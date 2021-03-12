## OVERVIEW
Spent a couple hours on and off on this. Most of it was code analysis because I saw this as a learning opportunity and that’s how I like to learn.

Ran with MVVM and clean architecture. Grew up with them and have yet to find new bread and butter. Always open and looking for new architecture though- currently looking into MVI with MvRx. However, MVVM and clean architecture is fine for a project like this- MVVM does respectable code separation and clean architecture becomes overkill. 

Only added a few additional libraries to support my design approach- namely lifecycle libraries and dagger. Otherwise I took the KISS principle and worked with what I had. 

Code base was fine. I had to move some stuff around- again- to support my design approach. Separated domain and presentation logic into their own modules, integrated dagger, moved logic out of the activity, and completed minor refactors of existing classes- all to improve defensive programming and achieve separation of concerns. Gradle dependencies could also be better managed.

## BASE FEATURE IMPLEMENTATION
###### Pagination
- Add a onPageChangeCallback to the viewPager. Use it to fetch data when it indicates that the user is at least THRESHOLD_SIZE away from completely consuming the list.
- Add a progressBar. Render it when fetching data and hide it on usecase callback. Let it act as a flag- prevent additional data fetching if progressBar is visible.
###### Alternating Usecases
- Add an instance flag to help getRestaurants() determine which usecase to invoke. Alternate it on every successful usecase.

## FRAMEWORK EDGE CASES
###### Lifecycle Handling
- Maintain count data and restaurant list in ViewModel.
- Perform initial fetching of data only if savedInstanceState == null
###### Presentation-level Network Handling
- Show snackbar whenever usecase fails to retrieve data.
- Prevent users from interacting with buttons if there are no more restaurants to consume.
- Show default image when Picasso fails to load.

## OTHER EDGE CASES
###### Repeating images in adapter.
- Adapter fails to update the image in the viewholder.
- Handled by setting a default image on restaurant.image.isNotBlank().
###### Repeating restaurants in adapter.
- One usecase fetches a restaurant already loaded by the other usecase.
- Handled by caching restaurants in a MutableSet.
###### User navigates to the end of the adapter if clicking quickly.
- User is quickly clicking buttons and navigates to the next page at the same time the adapter is being populated with a new list.
- Handled by preventing the user from interacting with buttons before usecase callback.
