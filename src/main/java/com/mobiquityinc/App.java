package com.mobiquityinc;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String ip = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";
        String regex = "\\d+\\s:(\\s\\(\\d+,\\d+.\\d+,(\\u20AC)\\d+\\))+";
        System.out.println(ip.matches(regex));
    }
}
