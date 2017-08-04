import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ResultsInterpreter {

    //ArrayList of keySets
    private final ArrayList<String[][]> sets = new ArrayList();
    private String[] keys;

    //Sets up key : value incrementing

    /***
     * Used to seperate the keys for incrementing
     * eg. [["a"],["b", "c"],["d", "e", "f"]]
     *        1        2            3
     * The numbers below corresponds to the response that causes the keys above to be incremented.
     *
     * @param keySets Should be like so [["key_1", ... , "key_n"], ... , keySet_n]
     * @return Returns this, so that functions can be stringed together
     */
    public ResultsInterpreter q (String[][] keySets) {

        /*for (String[] set : keySets) {
            for (String key : set) {
                System.out.print(key + " ");
            }
            System.out.println();
        }
        System.out.println();*/

        this.sets.add(keySets);
        return this;
    }

    /***
     * Used to get the score of some results against keys
     * eg. "p" : 4
     *     "j" : 11
     *     ...
     *     "foo" : n
     * Used in outputting survey messages based on the scores of each tested condition [probably better name for it]
     *
     * @param results The int[] results, where each value is indexed to a question number (-1)
     * @return A map containing k:v for tested conditions / the scores of the conditions
     */
    public Map getScore (int[] results) {

        //Use a map for k:v pairs to replace hard coded variables
        //eg. $p in paei.php
        Map<String, Integer> answerTally = new HashMap<String, Integer>();
        for (String key : keys) {
            answerTally.put(key, 0);
        }

        //In case of the number of results varies
        int max;
        if (results.length > sets.size()) {max = sets.size();} else {max = results.length;}
        //System.out.println("Max = " + max);
        for (int question = 0; question < max; question++) {

            //Results start at one, so remove one for proper indexing
            int result = results[question] - 1;
            //System.out.println(question + ".) result = " + (result + 1));
            String[][] keySets = sets.get(question);
            if (result < keySets.length) {

                //For every key specified in the set
                String[] keySet = keySets[result];
                for (String key : keySet) {

                    int tally = 1;
                    //Increment the value for the key
                    if (answerTally.get(key) != null) {
                        tally = answerTally.get(key) + 1;
                    }
                    answerTally.put(key, tally);

                }
            }
        }

        //Return the map containing the scores
        return answerTally;
    }

    //Constructor for within Java
    public ResultsInterpreter (String[] keys) {
        this.keys = keys;
    }

    //Constructor from file
    public ResultsInterpreter (String filePath) {

        /*
        1.) JSON Support (comp)
            --> Regardless of order

            {
                keys : ["a","b",...,"n"]
                1 : [["a"],["b"],["c","d"]]
                ... : ...
                n : [...]
            }

            Processing: Identify object
                        Split at :
                        Separate into arrays

        2.) Text Support
            --> Order based (simp)

            KEYS a b c d
            1. a b | c d
            2. a | b | c d
            a b c | d [NOTE: DOESN'T REQUIRE NUMBERING]

            Processing: [Identify KEYS]
                        Split at .
                        Split second array at |
                        Split those arrays at [SPACE]
                        Reorder into new ArrayList
        */

        String fileContents = "";
        if (filePath.endsWith(".txt") || filePath.endsWith(".json")) {

            try {
                FileReader fr = new FileReader(filePath);
                Scanner in = new Scanner(fr);
                while (in.hasNextLine()) {
                    fileContents = fileContents.concat(in.nextLine() + "\n");
                }
                in.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filePath);
                e.printStackTrace();
            }
            System.out.println(fileContents);
        }

        if (filePath.endsWith(".txt")) {
            //Scanner to 'pick apart' fileContents
            Scanner inpr = new Scanner(fileContents);

            //Text File Format
            System.out.println("USING TEXT FILE FORMAT");
            while (inpr.hasNextLine()) {
                String ln = inpr.nextLine();
                //Deal with keys
                if (ln.startsWith("KEYS")) {
                    ln = ln.replace("KEYS ", ""); //Remove KEYS
                    String[] exploded = ln.split(" ");
                    this.keys = exploded;

                    /*System.out.print("Keys: ");
                    for (String key : this.keys) {
                        System.out.print(key + " ");
                    }
                    System.out.println();*/

                }
                else {
                    String[] exploded = ln.split("\\."); //Separate number part

                    /*for (String sect : exploded) {
                        System.out.print(sect + ",");
                    }*/

                    try {
                        int num = Integer.parseInt(exploded[0]); //The question #
                        //TODO sort the sets based on num, so that questions can be unordered in the text file
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    exploded = exploded[1].split(","); //Separate into sets
                    /*
                       The text has followed a progression of [num][[keys],[keys],...,[keys]]
                       Now, the keys will be split up
                       -> [num][[[key],[key]...],[[key],[key]...],...,[[key],[key]...]]
                       Each key set ([[key],[key]...]) will be added to the array keySets
                       -> [[[key],[key]...],[[key],[key]...],...,[[key],[key]...]]
                       This can be used to produce the ArrayList 'sets'
                    */
                    String[][] keySets = new String[exploded.length][];
                    for (int i = 0; i < exploded.length; i++) {
                        String keys = exploded[i];
                        String[] keySet = keys.split(" "); //Separate set into keys
                        keySets[i] = keySet;
                    }
                    this.q(keySets); //Added to sets

                    /*for (String[] set : keySets) {
                        for (String key : set) {
                            System.out.print(key + " ");
                        }
                        System.out.println();
                    }
                    System.out.println();*/

                }

            }

        }

        else if (filePath.endsWith(".json")) {

            /*JSONParser parser = new JSONParser();

            try {

                Object obj = parser.parse(new FileReader("f:\\test.json"));

                JSONObject jsonObject = (JSONObject) obj;
                System.out.println(jsonObject);

                String name = (String) jsonObject.get("name");
                System.out.println(name);

                long age = (Long) jsonObject.get("age");
                System.out.println(age);

                // loop array
                JSONArray msg = (JSONArray) jsonObject.get("messages");
                Iterator<String> iterator = msg.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/

            ;

            JSONParser parser = new JSONParser();
            try {

                Iterator<String> stringIter;
                Object obj = parser.parse(fileContents);

                JSONObject jsonObject = (JSONObject) obj;
                JSONArray jsonKeys = (JSONArray) jsonObject.get("keys");
                String[] keys = new String[jsonKeys.size()];
                stringIter = jsonKeys.iterator();
                //Such a weird way of doing this
                int i = 0;
                while (stringIter.hasNext()) {
                    keys[i] = stringIter.next();
                    //System.out.println(keys[i]);
                    i++;
                }
                this.keys = keys;

                i = 1;
                while (jsonObject.containsKey(Integer.toString(i))) {
                    JSONArray jsonKeySets = (JSONArray) jsonObject.get(Integer.toString(i));
                    Iterator<JSONArray> arrayIter = jsonKeySets.iterator();
                    String[][] sets = new String[jsonKeySets.size()][];
                    //weird again
                    int k = 0;
                    while (arrayIter.hasNext()) {
                        JSONArray keySets = arrayIter.next();
                        String[] set = new String[keySets.size()];
                        for (int j = 0; j < keySets.size(); j++) {
                            System.out.println("k = " + k + ", j = " + j);
                            set[j] = (String) keySets.get(j);
                            System.out.println(set[j]);
                        }
                        sets[k] = set;
                        k++;
                    }
                    this.q(sets);
                    System.out.println();
                    i++;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    public String[] getKeys () {
        return this.keys;
    }

}
