# Genesis

A plugin inspired by the Origins mod for Fabric, but for Paper.

Compatible on Paper 1.20.2+

Code of Conduct is located in the [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) file.

## Building

Requirements:

- Java 17 JDK

Steps:

1. Clone repository
2. `./gradlew spotlessApply shadowJar`

## Code conventions/quality gates

This repository currently has a couple of checks in place to ensure code quality. These are:

- Syntax check powered by [Spotless](https://github.com/diffplug/spotless)
- [CodeQL](https://codeql.github.com/) analysis on pull requests
- [Dependabot](https://github.com/dependabot) to keep dependencies up to date

There is also no allowing of force pushing to the main branch. Everything has to be done through a pull request. If you have a new feature that you want to add, please create a new branch and open a pull request. This will allow for code review and discussion before merging.

## Required plugin dependencies

- [LuckPerms](https://luckperms.net/)
    - Used for setting origins as a meta.
- [WorldGuard](https://worldguard.enginehub.org/)
    - Used for marking regions as "safe" for certain origins.

## Configuration

There is a configuration file you can edit.

Default configuration is below:

```yaml
# When specifying duration there are 20 ticks in a second
# 'max-health', 'arrow-buff', 'water-damage' values must be doubles (ex: 16.0 and not 16)
classes:
  skeleton:
    max-health: 16.0
    daylight-fire-ticks: 60
    milk-buff:
      duration-ticks: 300
      amplifier: 0
    arrow-buff: 1.0
  enderman:
    max-health: 20.0
    ability-cooldown: 200
    water-damage: 1.0
    ranged-debuff: 2.0
  axolotl:
    max-health: 16.0
    ability:
      charge: 200
      slowness-level: 1
      regen-level: 1
    aquatic-mob-buff: 1.0
  sculk:
    max-health: 20.0
    ability:
      level-cost: 1
      health-reward: 2.0
    mining-fatigue-level: 1
    slowness-level: 1
  shulker:
    max-health: 28.0
    heavy-armor-slowness: 200
  phantom:
    max-health: 16.0
    daylight-fire-ticks: 60
    fall-damage-modifier: 0.48
```

Each class has a set of values that can be configured. Some classes have more modifiers than others.

### Class modifiers

_Work in progress_

## Commands

### `/genesis`

This opens the main menu for the plugin. This is where you can select your origin.

Permission is set to all players by default.

### `/genesisreload`

This reloads the configuration file.

### `/genesisreset`

This resets the player's origin.

## Permissions

_Work in progress_
