# 06: Yak Shaving Redux

Our next step in building this slice of authentication functionality is to have our frontend use it. That presents a challenge, as we don't actually have a frontend yet. Instead of starting from scratch, let's take advantage of some of the wonderful design work that others have released. Some basic parameters: 

- NPM+Vite+React
    - NPM since we're still small -- pnpm or yarn if we get a lot bigger
    - Vite for building, serving, and acting as an API proxy to our Java backend
    - React for our UI framework
- Material UI (mui)
    - Easy-to-use, well documented, and plenty of support available

This should provide a solid base foundation. I followed general scaffolding guidelines and utilized CoPilot to help generate the foundation. 

## Blog Articles

I've been writing these Markdown documents, but so far, haven't made efforts to share them. To that end, we're going to create some GitHub Actions scripts that will publish to our GitHub Pages. We'll use the same Material theme, and utilize mkdocs to convert our markdown to a static html site. 

You can see this result at https://dustinbarnes.github.io/connect-four-demo (hopefully where you're reading this!).
