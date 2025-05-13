package com.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    private static final String SOME_FORMAT = "RB%s %s";

    @Test
    public void shouldAnswerWithTrue() {
        Assertions.assertTrue( true );
    }


    /*
    Exercise: Manipulating a List of Numbers
    Objective: Given a list of integers, use the Stream API to perform the following operations:
    Filter out the even numbers.
    Multiply each even number by 2.
    Get the sum of all the resulting numbers
     */

    @Test
    public void test() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Integer result = numbers.stream()
                .filter( n -> n % 2 == 0 )
                .map( n -> n * 2 )
                .mapToInt( Integer::valueOf )
                .sum();

        System.out.println( result );

    }




    /*
     **
     *
     * Using the streams API, filter a list of strings to return a new list with only Strings with length >= 5:
     *
     * Ex. “abcde”, “a”, “abc”, “abcdef”, “abcdef”
     *
     * return: [“abcde”, "abcdef”, “abcdef”]
     **/



    @Test
    public void stringFilter() {
        List< String > strings = List.of( "abcde", "a", "abc", "abcdef", "abcdef" );
        List<String> result = strings.stream().filter( s -> s.length() >= 5 ).toList();
        System.out.println(result);
    }

    public static Map< Integer, Integer > calculateChange( double amount ) {
        List< Integer > bills = Arrays.asList( 1, 2, 5, 10, 20 );
        Map< Integer, Integer > result = new LinkedHashMap<>();
        Collections.reverse( bills );
        AtomicReference< Double > change = new AtomicReference<>( amount );
        double changeAux = amount;
        for ( Integer bill : bills ) {
            if ( changeAux >= bill ) {
                int amountOfBills = (int) changeAux / bill;
                changeAux = changeAux % bill;
                result.put( bill, amountOfBills );
            }
        }
        /*bills.forEach( bill -> {
            if ( change.get() >= bill ) {
                int amountOfBill = (int) ( change.get() / bill );
                change.set( change.get() % bill );
                result.put( bill, amountOfBill );
            }
        } );*/


        return result;

    }

    public static void prueba( ) {
//        Map< Integer, Integer > result = calculateChange( 729 );
//        result.forEach( ( key, value ) -> {
//            System.out.printf( "Bill/Coin Denomination: %s - Amount of Bill/Coin: %s%n", key, value );
//        } );
//        for ( Map.Entry entry : result.entrySet() ) {
//            System.out.printf( "Bill/Coin Denomination: %s - Amount of Bill/Coin: %s%n", entry.getKey(), entry.getValue() );
//        }

        List< Integer > input1 = Arrays.asList( 25, 25, 50 );
        List< Integer > input2 = Arrays.asList( 25, 100 );
        List< Integer > input3 = Arrays.asList( 25, 25, 50, 50, 100 );
        List< Integer > input4 = Arrays.asList( 25, 25, 50, 50, 25, 100 );
        List< Integer > input5 = Arrays.asList( 25, 25, 25, 100 );

        System.out.println( "Enough Money: " + enoughMoney( input1 ) ); // YES
        System.out.println( "----------------------------------------" );
        System.out.println( "Enough Money: " + enoughMoney( input2 ) ); // NO
        System.out.println( "----------------------------------------" );
        System.out.println( "Enough Money: " + enoughMoney( input3 ) ); // NO
        System.out.println( "----------------------------------------" );
        System.out.println( "Enough Money: " + enoughMoney( input4 ) ); // YES
        System.out.println( "----------------------------------------" );
        System.out.println( "Enough Money: " + enoughMoney( input5 ) ); // YES
        System.out.println( "----------------------------------------" );
    }

    public static String enoughMoney( List< Integer > input ) {


        int saved = 0;
        int totalChange = 0;
        int ticketPrice = 25;
        for ( int current : input ) {
            int change = 0;
            if ( current == ticketPrice ) {
                saved += current;
            }
            if ( current > ticketPrice ) {
                change = current - ticketPrice;
                saved = ( saved + current ) - change;
                totalChange += change;
            }
            if ( saved < totalChange ) {
                return "NO";
            }
            //System.out.println("Current Saved: " + saved +  " Returned:" + totalChange);
        }
        return "YES";
    }
}
