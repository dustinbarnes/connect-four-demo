# Bookkeeping

The point of this project is to highight a project, but also to tell a story. Software systems rarely exist in a vacuum, and a lot of the work in learning a new system is understanding how it came to be. 

The blog posts are meant as milestone markers. For each task I do, I intend to write a new post to explain what I did and why. 

In order to keep the storytelling going, I'm going to want to be able to show people various states of the project at these important milestones. I've decided that I'll only push a new blog file when I'm ready to share the work.

## Automating with GitHub Actions

We will be utilizing GitHub actions for our CI/CD tooling -- at least to start. 

Each time we push a new file in the blog/ directory, I want to tag the repository with the name of the file. 

This is a relatively straightforward GitHub Action. To that end, I have created [this action](../.github/workflows/tag-blog-file.yml). 

Committing this file will be the first test. 

In true development fashion, the first test failed. The action did not have permissions to write the tags back to the repository. I've updated those permissions, and now the tags are working well. 
