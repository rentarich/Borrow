package si.fri.rso.borrow.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestProperties {

    @ConfigValue(value = "maintenance", watch = true)
    private String maintenance;


    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String config_1_value) {
        this.maintenance = config_1_value;
    }
}
