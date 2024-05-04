package top.recordsite.system;

import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.recordsite.dto.system.MenuAddDto;
import top.recordsite.dto.system.MenuSearchDto;
import top.recordsite.dto.system.MenuUpdateDto;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.Result;
import top.recordsite.vo.system.MenuVo;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
@PreAuthorize("hasAnyAuthority('system:menu')")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @SystemLog(businessName = "获取后台table权限列表")
    @PostMapping
    public Result getMenuList(@RequestBody @Validated MenuSearchDto dto) {

        ListVo<MenuVo> listVo = menuService.getMenuList(dto);

        return Result.success(listVo);
    }

    @SystemLog(businessName = "获取后台权限列表")
    @GetMapping("/{id}")
    public Result getMenuInfo(@PathVariable("id") Integer id) {

        MenuVo menuVo = menuService.getMenuInfo(id);

        return Result.success(menuVo);
    }

    @SystemLog(businessName = "获取后台修改框权限列表")
    @GetMapping("/select")
    public Result getMenuTreeList() {

        List<MenuVo> list = menuService.selectAllRouterMenuTree();

        return Result.success(list);
    }

    @SystemLog(businessName = "修改权限")
    @PutMapping
    public Result updateMenu(@RequestBody @Validated MenuUpdateDto dto) {
        menuService.updateMenu(dto);

        return Result.success();
    }

    @SystemLog(businessName = "增加权限")
    @PostMapping("/add")
    public Result addMenu(@RequestBody @Validated MenuAddDto dto) {
        menuService.addMenu(dto);
        return Result.success();
    }

    @SystemLog(businessName = "删除权限")
    @DeleteMapping("/{ids}")
    public Result deleteMenu(@PathVariable("ids") @NotEmpty List<Integer> ids){
        menuService.deleteMenuByIds(ids);
        return Result.success();
    }

}
