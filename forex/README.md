# Paidy Interview #3 - Forex

## Assumptions

1. Whoever is running this project will provide their own 1Forge API key as I am hesitant to commit any credentials to a git repository
2. Only the 9 currencies / 72 currency pairs are intended to be available. The solution could be trivially altered to support all currency pairs available on 1Forge by dropping the `pairs` parameter from the 1Forge API request and removing the currency restrictions on the http endpoint exposed
3. If we were to call the 1Forge API for a single currency pair as frequently as once every 5 minutes this gives a worst case of 20736 API requests per 24 hours, well over the free tier of 1Forge. 
Instead we fetch all keypairs at once since this allows for any number of requests per second. 
4. I left in the Monix Task support for akka-http even though it is not being used, in a production environment this would probably be in a separate library so I felt it best to leave it unchanged

## Design Decisions

- Include `http4s` for a pure functional HTTP client that uses the cats effect monad
- Include `scalacache` with the `caffeine` in-memory backend. This gives us a simple cache with expiration and an `IO` interface to the cache (which is overkill since it is in memory, but preferable to having to mix the `Try` api into the `Eff` stack)
- Decided to stick with the `Eff[R, _]` extensible effects for the algebra although it seems like overkill for the simple use case. The stack used for `Eff` is only the `IO` monad from `cats-effect` and `Either`
- The `Eff` stack runner now returns `Future` as we don't use the monix `Task` anywhere else and `akka-http` takes a `Future`
- Cleaned up the currency model as the conversion from a string was not defined for all inputs. If support for more currencies was desired then it would make sense to let this use any 3 letter string and leave the validity check to the 1Forge interpreter
- Added the `-Xlint` and `-Xfatal-warnings` scalac flags

## Running the project

Set the `ONEFORGE_API_KEY` environment variable to a valid 1Forge API key.

To run the server `sbt run`
To run the tests `sbt test`

## Short cuts

- I'm not very familiar with the `grafter` library for DI. I included a mock Rates interpreter with the intention of using it to add unit tests for the http routes with `akka-http-testkit` given more time.
- There are a few dependency evictions that I would have like to sort out

## Next Steps

- Include integration tests for 1Forge service
- Include health check that checks the api quota endpoint to make sure that the service is still able to communicate with 1Forge
- Split the request to 1Forge into 3 requests that fetch 1/3 of the pairs. This would keep the prices as up to date as possible whilst still remaining under the free tier limits (288 requests per day * 3)
- Make use of a distributed cache backend for `scalacache` so that multiple copies of the service could be deployed without depleting the 1 Forge 