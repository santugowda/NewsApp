# NewsApp
NY Times Article Search App: A simple Android app displaying grid of news articles retrieved from the NY Times search API.

Home Screen:
- Allows the user to enter a search term and displays the results in either a list or grid view.
- Used ActionBar SearchView as the query box.
- Results should display:
  - Article thumbnail
  - Article headline
  - Selecting an item in search results should open the detail screen.
  - Have pagination of results

Detail Screen:
  - Displays a detailed view of article.
  - User can share a link to their friends or email it to themselves

Also handles robust error, check if internet is available, handle error cases, network failures

Libraries used :
* RecyclerView with the StaggeredGridLayoutManager to display the grid of image results
* GSON library to streamline the parsing of JSON data
* Android Async HTTP lib to handle the request and response
* Picasso for image rendering.
