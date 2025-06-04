# Authentication Backend

Now we're going to build the backend authentication endpoints. Let's get to it! 

## Hooking Up the Database

SQLite plays very well with Java. I generally don't find ORMs to be very useful, especially for small projects like this. When you have a large object graph that must be sync'd, then sure, pick up Hibernate or whatever. 

We also aren't going to go raw JDBC. We'll go with one of my favorite project: [jOOQ](https://www.jooq.org/). With a couple of dependencies: 

```
	implementation("org.jooq:jooq:3.19.7")
	implementation("org.xerial:sqlite-jdbc:3.45.3.0")
```

And a quick addition to our application.yml file: 

```
spring:
  datasource:
    url: jdbc:sqlite:../../database/dev.db
    driver-class-name: org.sqlite.JDBC
```

and that's all we really need to get started. We could create a trivial test, but trivial tests are trivial.

## Generating from the API

Let's start with defining our Auth tests. Doing this immediately is why I allowed myself to say that creating a trivial test isn't worth doing at this point. 

We have our API definition from before, we can use that definition to auto-generate Java classes. The magic is that we only generate API interfaces, and implement those interfaces. In addition, if anything significantly changes in our controller or spec, the build should fail. It forces us to (at least nominally) adhere to our contract. Using Gradle this turned out to be relatively easy: 

```
openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("${rootDir}/../openapi/auth.yaml")
	outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)
	apiPackage.set("com.github.dustinbarnes.connect_four_demo.backend.api")
	modelPackage.set("com.github.dustinbarnes.connect_four_demo.backend.model")
	invokerPackage.set("com.github.dustinbarnes.connect_four_demo.backend.invoker")
	configOptions.set(mapOf(
		"interfaceOnly" to "true",
		"useTags" to "true",
		"dateLibrary" to "java8",
		"useSpringBoot3" to "true",
		"generateSpringSecurity" to "true",
	))
}
sourceSets["main"].java.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/java"))
```

We tell the generator to generate the code to `build/generated/openapi` (the `src/main/java` is automatically added), then tell Gradle that our main source sets includes that directory as well. From here, we can add a minimal AuthController: 

```java
package com.github.dustinbarnes.connect_four_demo.backend.controller;

import org.springframework.web.bind.annotation.RestController;

public class AuthController implements AuthApi{

}
```

That's it. The OpenAPI generator creates default interface methods that fail. From here, we can have copilot generate test stubs. It did a pretty good job: 

```java
    @Test
    void registerUser_success() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
```

However, some of the other scenarios (like bad inputs) can't be tested because we haven't actually defined valid inputs! 

I'm going to wrap up this section with this minimal implementation. For next time, we'll have a failing test and we can red-green our way to a solid implementation! 
