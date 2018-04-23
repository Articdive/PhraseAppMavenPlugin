[![Build Status](http://ci.articdive.cf/job/PhraseApp-Maven-Plugin/badge/icon)](http://ci.articdive.cf/job/PhraseApp-Maven-Plugin)
# PhraseApp-Maven-Plugin
A Maven Plugin which allows you to retrieve PhraseApp locales on compile/build

# How to add this plugin to your Maven Project

Add this pluginRepository:
```
    <pluginRepositories>
        <pluginRepository>
            <id>Articdive's Repostiory</id>
            <name>phraseapp-maven-plugin Repository</name>
            <url>http://ci.articdive.cf/plugin/repository/everything/</url>
        </pluginRepository>
    </pluginRepositories>
```
Add this to your build configuration:
```
    <build>
        <plugins>
            <plugin>
                <groupId>de.articdive</groupId>
                <artifactId>phraseapp-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>phrase</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!-- Required Values -->
                    <!-- Please make sure your auth-token is a READ-ONLY token! 
                    (Important if you want to deploy to GitHub, Bitbucket e.t.c -->
                    <authToken>INSERT_AUTH_TOKEN_HERE</authToken>
                    <projectID>INSERT_PROJECT_ID_HERE</projectID>
                    <!-- Optional Values -->
                    <branch>INSERT_BRANCH_HERE</branch>
                    <ignoredLocales>
                        <ignoredLocale>INSERT_LOCALE_ID_TO_IGNORE_HERE</ignoredLocale>
                    </ignoredLocales>
                </configuration>
            </plugin>
        </plugins>
    </build>
```
