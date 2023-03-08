package net.galacticraft.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.authentication.http.BasicAuthentication
import org.gradle.api.java.archives.Manifest
import java.util.Date

class Util {
    
    static coreLibrary(Project project)
    {
        return project.dependencies.create("net.galacticraft.gradle:core:[1.0.0-,)");
    }

    static getGalacticPluginMaven(Project project) {
        return { MavenArtifactRepository it ->
            name 'GalacticPlugins'
            if(project.hasProperty('NEXUS_USER') && project.hasProperty('NEXUS_PASSWORD')) {
                url 'https://maven.galacticraft.net/repository/gradle-plugins/'
                authentication {
                    basic(BasicAuthentication)
                }
                credentials {
                    username = project.property('NEXUS_USER')
                    password = project.property('NEXUS_PASSWORD')
                }
            } else {
                url 'file://' + project.rootProject.file('repo').getAbsolutePath()
            }
        }
    }

    static getGalacticInternalMaven(Project project) {
        return { MavenArtifactRepository it ->
            name 'GalacticInternal'
            if(project.hasProperty('NEXUS_USER') && project.hasProperty('NEXUS_PASSWORD')) {
                url 'https://maven.galacticraft.net/repository/internal-plugins/'
                authentication {
                    basic(BasicAuthentication)
                }
                credentials {
                    username = project.property('NEXUS_USER')
                    password = project.property('NEXUS_PASSWORD')
                }
            } else {
                url 'file://' + project.rootProject.file('repo').getAbsolutePath()
            }
        }
    }

    static getManifest(Project project) {
        return { Manifest it ->
            attributes([
                'Implementation-Title'        : "GalacticGradle | " + project.name,
                'Implementation-Version'      : project.version,
                'Implementation-Vendor'       : "TeamGalacticraft",
                'Implementation-Timestamp'    : new Date().format("yyyy-MM-dd'T'HH:mm:ssZ")
            ])
        }
    }
    
    static getExcludeGroup() {
        return [
            'META-INF/*.SF', 'META-INF/*.DSA', 'META-INF/*.RSA',
            'META-INF/LICENSE*', 'META-INF/DEPENDENCIES',
            'META-INF/NOTICE*', 'META-INF/versions/**',
            'META-INF/maven/**', 'Log4j*', '_EMPTY_',
            'LICENSE*', 'META-INF/*.kotlin_module',
            'META-INF/services/**', 'META-INF/com.android.tools/**',
            'module-info.class', 'licenses/**', 'kotlin/**', 
            'proguard/**', 'MANIFEST.MF'
        ]
    }
}
