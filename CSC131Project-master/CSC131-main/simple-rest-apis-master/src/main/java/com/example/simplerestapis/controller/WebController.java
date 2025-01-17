package com.example.simplerestapis.controller;
import com.example.simplerestapis.Award;
import com.example.simplerestapis.Movies;
//import com.google.gson.annotations.SerializedName;
//import jdk.jfr.Category;
import com.example.simplerestapis.readCSV;
import org.springframework.web.bind.annotation.*;

import com.example.simplerestapis.models.PostRequest;
import com.example.simplerestapis.models.PostResponse;
import com.example.simplerestapis.models.SampleResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebController {

	private static final List<Movies> ALL_MOVIES = readCSV.all();
	private static final ArrayList<String> ALL_CATEGORIES = readCSV.get_Category();
	@GetMapping("/movies")
	public SampleResponse Sample(@RequestParam(value = "name",
	defaultValue = "null") String name) {
		SampleResponse response = new SampleResponse();
		response.setId(1927);
		response.setMessage("Your movie is "+name);
		return response;
	}
	/**
	 http://localhost:8080/movies/bestpicture/year/2000/winner
	 **/

	@CrossOrigin
	@GetMapping("/movies/{category}/year/{year}/{winner}")
	public ArrayList<Movies> movie(@PathVariable("category") String category,
	                               @PathVariable("year") String year
								   ){
		ArrayList<Movies> matchList = new ArrayList<>();
		ArrayList<Movies> matchYear = new ArrayList<>();
		for (Movies movie : ALL_MOVIES)
		{
			for (Award award : movie.getAwards()) {
				// Get the match Categories
				if (award.getCategory().toUpperCase().equals(category.toUpperCase())
						&& movie.getYear().equals(year)
						&& award.isWinner())
				{
					matchList.add(movie);
					break;
				}
			}
		}
		return matchList;
	}

	/**
	 * http://localhost:8080/movies/search?year=1997&category=bestpicture
	 * @param year
	 * @param category
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/movies/search")
	public ArrayList search(@RequestParam(value = "year", defaultValue= "null" ) String year,
					   @RequestParam(value = "category") String category )
	{

		ArrayList<Movies> matchSearch = new ArrayList<>();
		for (Movies movie : ALL_MOVIES)
		{
			for (Award award : movie.getAwards()) {
				// Get the match Categories
				if (movie.getYear().equals(year) && award.getCategory().toUpperCase().equals(category.toUpperCase()))
					matchSearch.add(movie);
					break;
			}
		}

		return  matchSearch;

	}

	/**
	 * http://localhost:8080/search?type=actor&year=1936&category=inasupportingrole
	 * @param type
	 * @param year
	 * @param category
	 * @return
	 */
	@GetMapping("/search")
	public ArrayList<Movies> search(@RequestParam(value = "type", defaultValue = "null") String type
			,@RequestParam(value = "year", defaultValue = "null") String year,
									@RequestParam(value = "category", defaultValue = "null") String category)
	{
		ArrayList<Movies> matchSearch = new ArrayList<>();

		for(Movies movie : ALL_MOVIES)
		{
			for(Award award : movie.getAwards())
			{
				if (award.getCategory().toUpperCase().contains(type.toUpperCase())
						&& movie.getYear().equals(year)
						&& award.getCategory().toUpperCase().contains(category.toUpperCase()))
				{
					matchSearch.add(movie);
					break;
				}
			}
		}
		return matchSearch;
	}

	/**
	 * http://localhost:8080/movies/categories
	 * @return
	 */
	@GetMapping("/movies/categories")
	public ArrayList<String> cat() {
		int j = 0;
		ArrayList<String> list = new ArrayList<String>();
		list.add(ALL_CATEGORIES.get(0));

		for(int i = 1; i <= ALL_CATEGORIES.size() - 2; i++)
		{
			int key = ALL_CATEGORIES.get(i).compareTo(list.get(j));
			if(key != 0)
			{
				j++;
				list.add(ALL_CATEGORIES.get(i));
			}
		}

		return list;
	}

	/**
	 * http://localhost:8080/winner
	 * @return
	 */

	@GetMapping("/winner")
	public List<Movies> winner()
	{
		ArrayList<Movies> won = new ArrayList<>();

		for (Movies movie : ALL_MOVIES)
		{
			for (Award award : movie.getAwards())
			{
				if (award.isWinner())
				{
					won.add(movie);
					break;
				}
			}
		}
		return won;
	}


	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public PostResponse Test(@RequestBody PostRequest inputPayload) {
		PostResponse response = new PostResponse();
		response.setId(inputPayload.getId()*100);
		response.setMessage("Hello " + inputPayload.getName());
		response.setExtra("Some text");
		return response;
	}
}
