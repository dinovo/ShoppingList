# ShoppingList
Shopping List App for Android class.

Requirements for the shopping app - 10 things.
 
1. The list should be displayed of course with both name and quantity of items.
2. Possibility to add new items to the list (with a name and quantity - so two input fields so you can input for instance “10” in one and “eggs” in the other). You must use a separate Product class ( or similar name) to store the items on the list.
3. Possibility to clear the entire shopping list (from the actionbar/overflow menu). Furthermore, when a user wants to clear the list, you should display a dialog asking “Are you sure” with yes/no buttons or similar and make the clearing only if the user presses yes. 
4. Possibility to delete an individual item from the list (and keeping the rest of course) AND giving the user the option to cancel the deletion by using a SnackBar (see relevant week for that)
5. Your app should have a custom launcher icon that you create yourself in the correct display densities.
6. Your app MUST support orientation change - so the list should not be cleared when the phone is turned. So your app should work in both orientations.
7. You must have a sharing action (in the actionbar/overflow menu) that can be used for sending (as text) the entire shopping list to another person by using a share Intent (topic for lecture 7 - the “Sharing Data via Intents” link from Google)
8. You must have a settings Activity that can be activated from the actionbar/overflow menu. In the settings you should at least have 1 setting for the user to change (what setting is up to you - it could be something as simple as the name of the user or it could be more advanced settings. BUT THE SETTING MUST HAVE SOME EFFECT IN THE APP).
9. The data in the shopping list should be saved to a persistent storage, so the data will also be there after the phone is turned off. For this you must use FireBase (lecture 10) and when the app starts it should load the data again. It is NOT a requirement to have user authentication. So we will have a one user model for simplification. Note: Firebase also works offline - and data is synced automatically when the user goes online again.
10. Email/password authentication with Firebase. 
