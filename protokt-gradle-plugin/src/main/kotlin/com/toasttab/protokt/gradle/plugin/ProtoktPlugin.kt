/*
 * Copyright (c) 2019 Toast Inc.
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

package com.toasttab.protokt.gradle.plugin

import com.toasttab.protokt.gradle.EXTENSIONS
import com.toasttab.protokt.gradle.configureProtokt
import com.toasttab.protokt.gradle.resolveProtoktCoreDep
import com.toasttab.protokt.util.getProtoktVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

val protoktVersion by lazy { getProtoktVersion(ProtoktPlugin::class) }

class ProtoktPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        configureProtokt(project) {
            project.afterEvaluate {
                project.configurations.named(EXTENSIONS) {
                    dependencies.add(
                        project.dependencies.create(
                            "com.toasttab.protokt:${project.resolveProtoktCoreDep()}:$protoktVersion"
                        )
                    )
                }
            }

            binaryFromArtifact(project)
        }
    }
}
