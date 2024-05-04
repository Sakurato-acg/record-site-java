-- 定义返回值，是个数组，包含：是否触发限流（1限流 0通过）、当前桶中的令牌数
local ret={}
ret[1]=0
-- Redis集群分片Key，KEYS[1]是限流目标
local cl_key = '{' .. KEYS[1] .. '}'

-- 获取限流惩罚的当前设置，触发限流惩罚时会写一个有过期时间的KV
-- 如果存在限流惩罚，则返回结果[1,-1]
local lock_key=cl_key .. '-lock'
local lock_val=redis.call('get',lock_key)
if lock_val == '1' then
    ret[1]=1
    ret[2]=-1
    return ret;
end

-- 这里省略部分代码

-- 获取[上次向桶中投放令牌的时间]，如果没有设置过这个投放时间，则令牌桶也不存在，此时：
-- 一种情况是：首次执行，此时定义令牌桶就是满的。
-- 另一种情况是：较长时间没有执行过限流处理，导致承载这个时间的KV被释放了，
-- 这个过期时间会超过自然投放令牌到桶中直到桶满的时间，所以令牌桶也应该是满的。
local last_time=redis.call('get',st_key)
if(last_time==false)
then
 -- 本次执行后剩余令牌数量：桶的容量- 本次执行消耗的令牌数量
    bucket_amount = capacity - amount;
    -- 将这个令牌数量更新到令牌桶中，同时这里有个过期时间，如果长时间不执行这个程序，令牌桶KV会被回收
    redis.call('set',KEYS[1],bucket_amount,'PX',key_expire_time)
    -- 设置[上次向桶中放入令牌的时间]，后边计算应放入桶中的令牌数量时会用到
    redis.call('set',st_key,start_time,'PX',key_expire_time)
    -- 返回值[当前桶中的令牌数]
    ret[2]=bucket_amount
    -- 无需其它处理
    return ret
end

-- 令牌桶存在，获取令牌桶中的当前令牌数
local current_value = redis.call('get',KEYS[1])
current_value = tonumber(current_value)

-- 判断是不是该放入新令牌到桶中了：当前时间-上次投放的时间 >= 投放的时间间隔
last_time=tonumber(last_time)
local last_time_changed=0
local past_time=current_time-last_time
if(past_time<inflow_unit)
then
 -- 不到投放的时候，直接从令牌桶中取走令牌
    bucket_amount=current_value-amount
else
 -- 需要放入一些令牌, 预计投放数量 = (距上次投放过去的时间/投放的时间间隔)*每单位时间投放的数量
    local past_inflow_unit_quantity = past_time/inflow_unit
    past_inflow_unit_quantity=math.floor(past_inflow_unit_quantity)
    last_time=last_time+past_inflow_unit_quantity*inflow_unit
    last_time_changed=1
    local past_inflow_quantity=past_inflow_unit_quantity*inflow_quantity_per_unit
    bucket_amount=current_value+past_inflow_quantity-amount
end

-- 这里省略部分代码

ret[2]=bucket_amount

-- 如果桶中剩余数量小于0，则看看是否需要限流惩罚，如果需要则写入一个惩罚KV，过期时间为惩罚的秒数
if(bucket_amount<0)
then
    if lock_seconds>0 then
        redis.call('set',lock_key,'1','EX',lock_seconds,'NX')
    end
    ret[1]=1
    return ret
end

-- 来到这里，代表可以成功扣减令牌，则需要更新令牌桶KV
if last_time_changed==1 then
    redis.call('set',KEYS[1],bucket_amount,'PX',key_expire_time)
 -- 有新投放，更新[上次投放时间]为本次投放时间
    redis.call('set',st_key,last_time,'PX',key_expire_time)
else
    redis.call('set',KEYS[1],bucket_amount,'PX',key_expire_time)
end
return ret