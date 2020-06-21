# BasedHits
BasedHits is an item based bounty plugin allowing players to place bounties/hits on other players. Upon running the '/hit' command the player will be prompted with an inventory to place their bounty in.

*current download: https://github.com/Ryxians/BasedHits/releases/tag/2.0-SNAPSHOT*

*Spigot page: SOON*

## Commands
'/hit <playername>' will place a hit on a player. The player may also use the aliases:
[hits, bounty, bounties]

'/hit <playername> a' will place an anonymous hit on a player, works with all hit aliases. Requires permission to BasedHits.anonymous.hit

'/view' will open a menu with all active bounties (currently limited to 54 at max)

'/view <playername>' will view a specific players bounty.

## Permissions
```
BasedHits.hit:
    description: Allows you to use the /hit command
    default: true
  BasedHits.view:
    description: Allows you to use the /view command
    default: true
  BasedHits.claim:
    description: Allows you to claim a bounty
    default: true
  BasedHits.broadcast:
    description: Allows you to see announcements of hits
    default: true
  BasedHits.anonymous.hit:
    description: Allows a player to have their hits hidden
    default: false
  BasedHits.anonymous.claim:
    description: Allows a player to have a claim hidden
    default: false
```

## Config
Expiration in config:
```yaml
# Delay in ticks till expiration (http://mapmaking.fr/tick/)
expiration: 5184000
```
5184000 is in ticks, translates to 3 days.

## To do
1. Create multiple viewing pages
2. Black/whitelist in config
3. Add an ingame way to delete hits (staff)
4. A way for people to claim their expired hits
5. Being able to sort hits by how new they are
6. Permission levels for how many hits a person can levy

