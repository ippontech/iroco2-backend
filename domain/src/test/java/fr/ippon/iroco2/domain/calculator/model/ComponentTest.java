/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.domain.calculator.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.ippon.iroco2.domain.calculator.model.emu.SettingName.INSTANCE_NUMBER;
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
