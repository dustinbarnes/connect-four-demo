# Inception

I need/want a project that can highlight some of my skills and expertise. Since most of my work is non-public, it can be a struggle to find that sweet spot of having enough complexity, but not too much. 

After some deliberation, I figured a web-based connect-four style game can do this. I can highlight the primary components of building a service that can scale -- a Java/Spring Boot backend, a Vite/React frontend, a Playwright e2e test suite, and an API-first approach. The build and structure of this application is mildly obtuse so that it mimics some of the real-world pains of projects at scale. 

## API First

This is an approach I've long espoused, so let's put our money where our mouth is. We want to start with an API definition, use those definitions to generate code, then tie that code together with our logic. 

We will define our HTTP endpoints, core DTOs, and message objects with an OpenAPI spec. We'll spend a good amount of time iterating on this spec before putting down too much code. 

## Future Plans

The point of this project is to highlight work efforts, so I expect that some major things will change. The only real plan is to follow this effort where it takes me, and to document the process along the way.
