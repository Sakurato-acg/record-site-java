package top.recordsite.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class IpUtils {

    @Autowired
    private RestTemplate restTemplate;

    public String getLocation(String ip) {
        Map info = restTemplate.getForObject("https://api.vore.top/api/IPdata?ip=" + ip, Map.class);
        String adcode = (String) ((Map) info.get("adcode")).get("n");
        return adcode;
    }

}
