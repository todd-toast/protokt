/*
 * Copyright (c) 2020 Toast Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toasttab.protokt.ext

import com.google.auto.service.AutoService
import com.toasttab.protokt.StringValue
import java.time.LocalDate

@AutoService(Converter::class)
object LocalDateStringValueConverter : Converter<LocalDate, StringValue> {
    override val wrapper = LocalDate::class

    override val wrapped = StringValue::class

    override fun wrap(unwrapped: StringValue) =
        LocalDateConverter.wrap(unwrapped.value)

    override fun unwrap(wrapped: LocalDate) =
        StringValue { value = LocalDateConverter.unwrap(wrapped) }
}
