package com.databaseassignment.app;
import org.apache.commons.cli.*;
import java.util.ArrayList;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
	Options options = new Options();

        Option pagesize = new Option("p", "pagesize", true, "page size");
        input.setRequired(true);
        options.addOption(pagesize);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return;
        }

        String pageFileSize = cmd.getOptionValue("pagesize");
	ArrayList<String> leftOverArgs = cmd.getArgList();
        String inputFilePath = leftOverArgs.get(0);

        System.out.println(inputFilePath);
        System.out.println(pageFileSize);
    }
}
