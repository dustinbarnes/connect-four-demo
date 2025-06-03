# Yak Shaving

I refer you to [this YouTube video](https://www.youtube.com/watch?v=CrJUhtHdGQ8), which features a clip from the TV show *Malcolm in the Middle*. In it, Hal has to change a light bulb but he keeps running into other things he has to do first, to the point that he's working on his car engine to replace the light bulb. 

I've also heard it more directly as: 

> [MIT AI Lab, after 2000: orig. probably from a Ren & Stimpy episode.] Any seemingly pointless activity which is actually necessary to solve a problem which solves a problem which, several levels of recursion later, solves the real problem you're working on.

If I went from our current state to our next state, which is generated API stubs that pair with our implementation code, you'd find me making decisions about Maven vs Gradle, persistence layers, other things that feel unrelated to implementing an API, but are the annoying necessary things that need to be in place. 

## Project Basics

The first choices cover the basics, and may or may not be set for you at your organization. Since this is greenfield project, we'll go with the newest stuff:

- Java 21
- Spring Boot 3
    - Generate controller from OpenAPI spec
    - with Spring Web
    - with Spring Security
- Gradle w/ Kotlin (see below)

At this point, in your organization, you may have project starter templates to help with generating new projects. I do not, so I used [Spring Initializr](https://start.spring.io/) which allows you to create a project very quickly. The generated project was extracted to the 'backend/' directory. 

### Gradle vs Maven

I have long espoused the benefits of Maven, where I generally think builds should be declarative instead of imperative, it should be hard to do bad things, and the "boringness" of Maven translates to the tooling being extremely capable and mature. I worried that the freedom of Gradle will create more unique snowflake builds, that too much custom logic will seep into the builds, that undisciplined developers wouldn't take the time to understand the tooling, but instead hack in solutions, all leading to an eventual problem when builds are no longer idempotent.

However, I haven't given Gradle a genuine shot in several years. Many, many smart people have migrated. It's hard to argue with the skill, scope, or scale of things like Android and Spring Boot. Therefore, I want to challenge myself and give this a genuine shot.

## Persistence

In order to keep persistance light at this point, we're going with the venerable SQLite. A very capable, extraordinarily stable SQL server that operates from a single file. It's not a database for scaling out to "web scale", but it's very quick and uses minimal resources. Its fitness for purpose for a small project like this? Perfect. 

We still want to practice API first, and I think the tool 'atlas' is a solid fit here. It allows you to define your SQL schema as code, and then it will create migration files for you that you can check in and execute. 

I've created a minimal Users table, and you can run `npm run build` in the terminal to perform the schema application. From here on out, you just modify the `schema.hcl` file, and atlas will take care of migrations. 

The first migration looks like this: 

```
> database@1.0.0 build
> atlas schema apply --url "sqlite://dev.db" --to file://schema.hcl

Planning migration statements (2 in total):

  -- create "users" table:
    -> CREATE TABLE `users` (
         `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
         `username` text NOT NULL,
         `password_hash` text NOT NULL,
         `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
       );
  -- create index "username_unique" to table: "users":
    -> CREATE UNIQUE INDEX `username_unique` ON `users` (`username`);

-------------------------------------------

Applying approved migration (2 statements in total):

  -- create "users" table
    -> CREATE TABLE `users` (
         `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
         `username` text NOT NULL,
         `password_hash` text NOT NULL,
         `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
       );
  -- ok (1.033125ms)

  -- create index "username_unique" to table: "users"
    -> CREATE UNIQUE INDEX `username_unique` ON `users` (`username`);
  -- ok (59.375µs)

  -------------------------
  -- 1.146458ms
  -- 1 migration
  -- 2 sql statements
```

You'll find this work in the [../database](../database) directory. 