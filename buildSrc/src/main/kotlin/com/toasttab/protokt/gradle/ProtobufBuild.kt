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

package com.toasttab.protokt.gradle

import com.google.protobuf.gradle.GenerateProtoTask
import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.bundling.Jar
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import java.net.URLEncoder

internal fun configureProtobufPlugin(project: Project, ext: ProtoktExtension, binaryPath: String) {
    project.apply(plugin = "com.google.protobuf")

    project.protobuf {
        generatedFilesBaseDir = "${project.buildDir}/generated-sources"

        configureSources(project, generatedFilesBaseDir)

        protoc {
            artifact = "com.google.protobuf:protoc:${ext.protocVersion}"
        }

        plugins {
            id("protokt") {
                path = normalizePath(binaryPath)
            }
        }

        generateProtoTasks {
            for (task in all()) {
                task.builtins {
                    findByName("java")?.run(::remove)
                }

                task.plugins {
                    id("protokt") {
                        project.afterEvaluate {
                            option("$KOTLIN_EXTRA_CLASSPATH=${extraClasspath(project, task)}")
                            option("$RESPECT_JAVA_PACKAGE=${ext.respectJavaPackage}")
                            option("$GENERATE_GRPC=${ext.generateGrpc}")
                            option("$ONLY_GENERATE_GRPC=${ext.onlyGenerateGrpc}")
                            option("$LITE=${ext.lite}")
                            option("$ONLY_GENERATE_DESCRIPTORS=${ext.onlyGenerateDescriptors}")
                        }
                    }
                }
            }
        }
    }
}

private fun extraClasspath(project: Project, task: GenerateProtoTask): String {
    var extensions: FileCollection = project.configurations.getByName(EXTENSIONS)

    if (task.isTest) {
        extensions += project.configurations.getByName(TEST_EXTENSIONS)
    }

    return extensions.joinToString(";") { URLEncoder.encode(it.path, "UTF-8") }
}

private fun configureSources(project: Project, generatedSourcesPath: String) {
    project.afterEvaluate {
        if (project.tasks.findByName("sourcesJar") != null) {
            tasks.named<Jar>("sourcesJar").configure {
                from("$generatedSourcesPath/main")
            }
        }
    }
}

private fun normalizePath(binaryPath: String) =
    if (OperatingSystem.current().isWindows) {
        // on windows, protoc expects a full, /-separated path to the binary
        binaryPath.replace('\\', '/') + ".bat"
    } else {
        binaryPath
    }
