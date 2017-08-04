import java.util.Map;

public class Main {

    //Generates some data in a 1,2,1,2,... pattern
    private static int[] getResults (int setSize) {
        int[] results = new int[setSize];
        for (int i = 0; i < setSize; i++) {
            int result;
            result = i % 2 == 0 ? 1 : 2;
            results[i] = result;
        }
        return results;
    }

    public static void main (String[] args) {

        int results[] =  {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1};
        System.out.println("no of results = " + results.length);

        for (int result : results) {
            System.out.print(result + " ");
        }
        System.out.println("\n--- --- --- --- ---");

        String[] keys = {"a","b","c","d"};
        ResultsInterpreter ri = new ResultsInterpreter(keys);
        Map tally;
        /*ri      .q(new String[][] {{"a"},{"b"},{"c"}})
                .q(new String[][] {{"a","b"},{"d"}})
                .q(new String[][] {{"b"},{"c","d"},{"a"},{"d"}});

                /*.q(new String[][] {{"a"},{"i"}})
                .q(new String[][] {{"i"},{"p"}})
                .q(new String[][] {{"a"},{"e"}})
                .q(new String[][] {{"p","a"},{"p","e"}})
                .q(new String[][] {{"a","i"},{"e","i"}})
                .q(new String[][] {{"p","a"},{"p","i"}})
                .q(new String[][] {{"a","e"},{"e","i"}})
                .q(new String[][] {{"p","a"},{"a","e"}})
                .q(new String[][] {{"a","e"},{"p","a"}})
                .q(new String[][] {{"a","e"},{"a","i"}})
                .q(new String[][] {{"e","i"},{"p","i"}})
                .q(new String[][] {{"p","a"},{"e","i"}})
                .q(new String[][] {{"a","i"},{"p","i"}})
                .q(new String[][] {{"p","e"},{"p","i"}})
                .q(new String[][] {{"p","e","i"},{"a"}})
                .q(new String[][] {{"p","e"},{"a","e"}})
                .q(new String[][] {{"e","i"},{"p","e"}})
                .q(new String[][] {{"p","e"},{"a","i"}})
                .q(new String[][] {{"p","a","e"},{"p","a","i"}})
                .q(new String[][] {{"p","e","i"},{"a","e","i"}})
                .q(new String[][] {{"p","a","e"},{"p","e","i"}})
                .q(new String[][] {{"p","a","i"},{"a","e","i"}})
                .q(new String[][] {{"p","a","e"},{"a","e","i"}})
                .q(new String[][] {{"p","a","i"},{"p","e","i"}});

        tally = ri.getScore(results);
        for (String key : keys) {
            System.out.println(key + " = " + tally.get(key));
        }
        System.out.println();

        results = getResults(27);
        tally = ri.getScore(results);
        for (String key : keys) {
            System.out.println(key + " = " + tally.get(key));
        }*/

        ResultsInterpreter rs2 = new ResultsInterpreter("C:\\Users\\eisstudent\\IdeaProjects\\Test\\src\\main\\paei.txt");
        tally = rs2.getScore(results);
        for (String key : rs2.getKeys()) {
            System.out.println(key + " = " + tally.get(key));
        }

        /*ResultsInterpreter rs3 = new ResultsInterpreter("C:\\Users\\eisstudent\\IdeaProjects\\Test\\src\\main\\text.json");
        tally = rs3.getScore(results);
        for (String key : rs2.getKeys()) {
            System.out.println(key + " = " + tally.get(key));
        }*/
    }

}
