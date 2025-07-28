package fr.ippon.iroco2.domain.calculateur.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.ippon.iroco2.domain.calculateur.model.emu.SettingName.INSTANCE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ComponentTest {
    @Test
    void getMemoryInMegaByte_should_return_0_if_empty_conf() {
        //given
        Component component = Component.create(null, null, null, null, List.of());
        //when
        var result = component.getMemoryInMegaByte();
        //then
        assertThat(result).isEqualTo(0d);
    }

    @Test
    void getMemoryInMegaByte_should_return_0_if_conf_without_setting() {
        //given
        Component component = Component.create(null, null, null, null, List.of(mock(ConfiguredSetting.class)));
        //when
        var result = component.getMemoryInMegaByte();
        //then
        assertThat(result).isEqualTo(0d);
    }

    @Test
    void getValue_should_return_null_if_no_conf() {
        //given
        Component component = mock(Component.class);
        //when
        var result = component.getValue(INSTANCE_NUMBER);
        //then
        assertThat(result).isNull();
    }

    @Test
    void getValue_should_return_null_if_conf_not_found() {
        //given
        var confs = List.of(mock(ConfiguredSetting.class));
        Component component = Component.create(null, null, null, null, confs);
        //when
        var result = component.getValue(INSTANCE_NUMBER);
        //then
        assertThat(result).isNull();
    }

    @Test
    void getValue_should_return_corresponding_value() {
        //given
        var sett = new ConfiguredSetting(null, INSTANCE_NUMBER, "666");
        var confs = List.of(mock(ConfiguredSetting.class), sett);
        Component component = Component.create(null, null, null, null, confs);
        //when
        var result = component.getValue(INSTANCE_NUMBER);
        //then
        assertThat(result).isEqualTo("666");
    }
}
