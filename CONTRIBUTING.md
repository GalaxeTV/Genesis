# Contributing Guidelines

## Open source license

This project is licensed under the [MIT license](LICENSE). By contributing to this project, you agree that your contributions will be licensed under its MIT license.

## Code of Conduct

For more information, see the [Code of Conduct](CODE_OF_CONDUCT.md).

The Code of Conduct is adapted from the [Contributor Covenant](http://contributor-covenant.org), version 2.0, available at [http://contributor-covenant.org/version/2/0](http://contributor-covenant.org/version/2/0/).

## How to contribute

To contribute to this project, we accept changes to this project with a pull request.

### Adding a new feature

To add a new feature to this project, follow these steps:

1. Fork the repository
2. Create a new branch for the feature
3. Commit your changes
4. Push the branch to your fork
5. Create a pull request
6. Wait for the pull request to be reviewed and merged
7. Delete the branch
8. Pull the latest changes from the main repository
9. Delete the fork
10. Celebrate!

GitHub is a great tool for collaboration. If you are not familiar with it, you can learn more about it [here](https://guides.github.com/activities/hello-world/).

We follow GitHub Flow for our development process. For more information, see [Understanding the GitHub Flow](https://guides.github.com/introduction/flow/).

Branches are named using the following convention:

- `feature/` for new features
- `bugfix/` for bug fixes
- `release/` for releases

Other branches may be created as needed.

### Reporting a bug

To report a bug, follow these steps:

1. Search the [issues](https://github.com/galaxetv/galaxesmp-plugin/issues) to see if the bug has already been reported
2. Create a new issue
3. Describe the bug
4. Wait for the bug to be fixed
5. Celebrate!

Bugs are fixed using the following process:

1. Create a new branch for the bug fix
2. Commit your changes
3. Push the branch to your fork
4. Create a pull request
5. Wait for the pull request to be reviewed and merged
6. Delete the branch
7. Pull the latest changes from the main repository
8. Delete the fork

Bugs are also reported through the Galaxe Discord server and can be linked to the issue.

### Submitting a pull request

Pull requests are accepted with the following process:

* Builds must pass
* Code must be formatted using the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
  * We use [Spotless](https://github.com/diffplug/spotless) to enforce this
  * You can run `./gradlew spotlessApply` to automatically format your code
* CodeQL must pass
* Qodana must pass
* Manual review must be completed by two maintainers or from `@galaxetv/crew`

Pull requests should be descriptive and be clearly documented. Pull requests should be small and concise.

Documentation is generated using [Javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html). Documentation should be added to all public methods and classes.

### Validation on server

To validate changes to the plugin, we will be using this plugin on a test server. This is after the pull request has been merged.

This will help us to ensure that the plugin is working as intended, and if we catch any issues we can resolve before making a release and pushing to the production server.
