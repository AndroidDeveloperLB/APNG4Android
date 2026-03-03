import java.util.Properties
import java.io.FileInputStream

apply(plugin = "maven-publish")

val uploadProperties = Properties()
uploadProperties.load(rootProject.file("scripts/maven.properties").inputStream())

val pomPropertiesFile = file("${projectDir}/pom.properties")
if (pomPropertiesFile.exists()) {
    uploadProperties.load(FileInputStream(pomPropertiesFile))
}

val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    uploadProperties.load(localPropertiesFile.inputStream())
}

var user: String? = System.getenv("BINTARY_USER")
var apiKey: String? = System.getenv("BINTARY_APIKEY")
var repo: String? = System.getenv("BINTARY_REPO")

if (user.isNullOrEmpty()) {
    user = uploadProperties.getProperty("user")
}

if (apiKey.isNullOrEmpty()) {
    apiKey = uploadProperties.getProperty("apiKey")
}

if (repo.isNullOrEmpty()) {
    repo = uploadProperties.getProperty("releasesRepository")
}

afterEvaluate {
    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("release") {
                groupId = uploadProperties.getProperty("groupId")
                artifactId = uploadProperties.getProperty("artifactId")
                version = rootProject.extra["Version"] as String
                
                pom {
                    name.set("APNG4Android")
                    description.set("Android animation support for APNG & Animated WebP & Gif")
                    url.set("https://github.com/penfeizhou/APNG4Android")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("osborn")
                            name.set("Pengfei Zhou")
                            email.set("pengfeizhou@foxmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git@github.com:penfeizhou/APNG4Android.git")
                        url.set("https://github.com/penfeizhou/APNG4Android")
                    }
                }
                
                from(components["release"])
            }
        }
        repositories {
            if (!repo.isNullOrEmpty()) {
                maven {
                    name = "MavenCentral"
                    url = uri(repo!!)
                    credentials {
                        username = user
                        password = apiKey
                    }
                }
            }
        }
    }
}
