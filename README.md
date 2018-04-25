# PhraseApp-Maven-Plugin
A Maven Plugin which allows you to retrieve PhraseApp locales on compile/build   
[![Build Status](http://ci.articdive.cf/job/PhraseApp-Maven-Plugin/badge/icon)](http://ci.articdive.cf/job/PhraseApp-Maven-Plugin)

# How to add this plugin to your Maven Project

Add this pluginRepository:
```xml
    <pluginRepositories>
        <pluginRepository>
            <id>Articdive's Repostiory</id>
            <name>phraseapp-maven-plugin Repository</name>
            <url>http://ci.articdive.cf/plugin/repository/everything/</url>
        </pluginRepository>
    </pluginRepositories>
```
Add this to your build configuration:
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>de.articdive</groupId>
                <artifactId>phraseapp-maven-plugin</artifactId>
                <version>2.0</version>
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
                    <include_empty_translations>true/false</include_empty_translations>
                    <fallback_locale_id>INSERT_FALLBACK_LOCALE_ID</fallback_locale_id>
                </configuration>
            </plugin>
        </plugins>
    </build>
```
# FAQ

## How can I create an authentication token on PhraseApp?

Log into PhraseApp hover over your name in the top right-hand corner and hit "Access Tokens" on the dropdown.    
Next hit "Generate Token", remove "write" from the scopes and give it some name e.g "maven-phraseapp-plugin".    

## Why do you want a "read-only" token?

If you don't post this to GitHub/BitBucket/Other then you can use a token with full permissions if you want.    
I mainly do it because most projects will end up on GitHub and anyone can take the key and write to your file.   

## I have an error, what do I do?

Feel free to open an issue request so I can see what the issue is and tell you how to fix it.   
However 80% of the issues are HTTP-Request Issues, their errors are logged so you might be able to fix them yourself.    
