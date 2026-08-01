package top.blogapi.admin.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.admin.domain.entity.SiteSetting;
import top.blogapi.admin.domain.service.SiteSettingService;
import top.blogapi.admin.interfaces.controller.SiteSettingAdminController.SettingEntry;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateSettingsCommand {

    private final SiteSettingService siteSettingService;

    @Transactional
    public void execute(List<SettingEntry> entries) {
        entries.forEach(entry -> {
            SiteSetting setting = new SiteSetting();
            setting.setKey(entry.key());
            setting.setValue(entry.value());
            setting.setType(entry.type());
            setting.setDescription(entry.description());
            siteSettingService.save(setting);
        });
    }
    static int mod = 1_000_000_007;
    public static void main(String[] args) {
        System.out.println(subsetXORSum(
               new int[]{1,3}
        ));
    }

    public static int subsetXORSum(int[] nums) {
        int ans=0;
        List<Integer> l = new ArrayList<>();
        int p = 0;
        for(int n: nums){
            p^=n;
            ans+=p;
            l.add(p);
            int e = l.size()-1;
            for(int i = 0; i<e;i++){
                int a = p^l.get(i);
                ans+=a;
                l.add(a);
            }
        }
        return ans;
    }


}
