 
package org.jsoup.examples;
import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
//main class
public class textRanking {
	/** First 15 links in order of appearance. 
	The only function that uses the webURL variable*/
	public static List<String> findLinks(String input, int count) 
	throws IOException
	{
		List<String> linkList = new ArrayList<String>();
		Document doc = Jsoup.connect(input).get();
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			linkList.add(link.attr("abs:href"));
		}
		List<String> arrList = linkList.subList(0, count);
		return arrList;
	}
	/**This function returns an integer of how many times
	the input string is found in the text.*/
	public static int Count(String text, String word)
	{
		int counter = 0;
		String msg[] = text.split(" ");
		//wordC = word with uppercase letter
		String wordC = word.toUpperCase();
		// Iterating the string using for each loop
		for (String words : msg) {
		    // If desired word is found
			if (words.equals(word)||words.equals(wordC)) {
				counter++;
			}
		}
		return counter;
	}
	/**This function returns a string of the position of each 
	desired word found in the text.*/
	public static String Position(String text, String word)
	{
		//Declare key variables.
		int position = 0;
		String new_str = "";
		//wordC = word with uppercase letter
		String wordC = word.toUpperCase();
		String msg[] = text.split(" ");
		for (String words : msg) {
			position ++;
		    // If desired word is found add position to string "new_str". 
			if (words.equals(word)||words.equals(wordC)) {
				//convert int type to string to concatenate
				String i = String.valueOf(position);
				//add to return value string
				new_str = new_str.concat(i).concat(" ");
			}
		}
		return new_str;
	}
	/**First 15 words by alphabetical order,
	 and their counts and list of positions, 
	 with the words in alphabetical order.*/
	 public static List<String> firstAlpha(String text, int count)
	 {
	 	List<String> alphaList = new ArrayList<String>();
	 	String msg[] = text.split(" ");
	 	for (String words : msg) {
	 		boolean ans = alphaList.contains(words);
			// If word is not already in word list, if statement 
			//adds missing word to list.
	 		if (!ans) {
	 			alphaList.add(words);
	 		}
	 	}
	 	//alphabetically sort word list
	 	Collections.sort(alphaList);
	 	//returns however many words as specified
	 	List<String> arrList = alphaList.subList(1, count);
	 	return arrList;
	 }
	/** First 15 words by count and 
	their counts, in descending order by count:".*/
	public static Map<String, Integer> firstCount(String text, int count)
	{
		//create a hasmap by iterating through 
		//list of words and attatching their count  to them.
		Map<String, Integer> map = new HashMap<>();
		//Iterate through cleaned string and put into map/dictionary
		String msg[] = text.split(" ");
		for (String words : msg) {
			words = words.replaceAll("[^A-Za-z]", "");
			map.put(words, Count(text, words));
		}
		//call function to sort by descending order
		map = sortByValue(map);
		return map;
	}
	/** Function to sort map values in descending order For Count
	*/
	public static Map<String, Integer> sortByValue(Map<String, Integer> hashm)
	{
		Map<String, Integer> sorted = hashm
		.entrySet()
		.stream()
		.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		.collect(
			toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
				LinkedHashMap::new));
		return sorted;
	}
	/** Main method. Here we recieve user input 
	and read URL and parse it into a string. 
	We then call the other required functions. 
	Key Variables:
	String text- all the text of HTML doc
	String webURL - the URL input by user
	*/
	public static void main(String[] args) 
	throws Exception{
		///Declaring the three lists that will be used to store collected data.
		List<String> alphaList = new ArrayList<String>();
		//https://en.wikipedia.org/wiki/Alice%27s_Adventures_in_Wonderland
		//Here we accept input for URL.
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the URL: ");
		String webURL = scan.nextLine();
		System.out.print("How many items would you like to collect: ");
		String number = scan.nextLine();
		int num = Integer.parseInt(number);  

		// Here we created a String variable of the HTML doc to parse.
		URL url = new URL(webURL);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream()));
		String inputLine;
		String toParse = "";
		//Here we iterate through the web page, creating a variable to parse.
		while ((inputLine = in.readLine()) != null)
			toParse = toParse.concat(inputLine);
		in.close();
		//Here we Parse
		Document doc = Jsoup.parse(toParse);
		String text = doc.text();
		String parsed =  "";
		String msg[] = text.split(" ");
		for (String words : msg) {
			words = words.replaceAll("[^A-Za-z]", "");
			words = words.toLowerCase();
			parsed = parsed.concat(words).concat(" ");
		}
	 	//System.out.println(parsed);// ALL THE TEXT VARIABLE. CLEANED
		System.out.print("\nFirst " + number + " links in order of appearance: \n\n");
		List<String> x = findLinks(webURL, num);
		for (String link : x) {
			System.out.println(link);
		}
		System.out.print("\nFirst " + number + " words by alphabetical order, and their counts and list of positions, with the words in alphabetical order: \n\n");
		List<String> y = firstAlpha(parsed, num);
		for (String word : y) {
			System.out.println("Word: " + word + "\t\t" + "Count: " + Count(parsed, word) + "\t\t" + "Position: " + Position(parsed, word) + "\n");
		}
		System.out.print("\nFirst " + number + " words by count and their counts, in descending order by count: \n\n");
		Map<String, Integer> map = firstCount(parsed, num);
		map.remove("");
		map.remove("p");
		int pointer = 0;
		for (Map.Entry<String, Integer> pair : map.entrySet()) {
			pointer++;
			if (pointer > num){
				System.exit(0);
			}
			System.out.println(String.format("Word: %s \t\t Count: %s", pair.getKey(), pair.getValue()));   
		}
	}
}
