package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiating all Buttons on the grid
        Button bt1 = findViewById(R.id.button1);
        Button bt2 = findViewById(R.id.button2);
        Button bt3 = findViewById(R.id.button3);
        Button bt4 = findViewById(R.id.button4);
        Button bt5 = findViewById(R.id.button5);
        Button bt6 = findViewById(R.id.button6);
        Button bt7 = findViewById(R.id.button7);
        Button bt8 = findViewById(R.id.button8);
        Button bt9 = findViewById(R.id.button9);

//        Initializing the array for the numbers on the buttons and the buttons themselves
        Integer[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        Button[][] btArr = {{bt1, bt2, bt3}, {bt4, bt5, bt6}, {bt7, bt8, bt9}};
        shuffleArr(nums); // Shuffle the numbers at the creation of every new game

        int k = 0;

//        looping through the buttons
        for(int i = 0; i < btArr.length; i++){
            for(int j = 0; j < btArr[i].length; j++){

                Button button = btArr[i][j];

//                Initializing the blank button if the number is zero to produce a random blank button
                if(nums[k] == 0) {
                    btArr[i][j].setText("");
                } else {
                    //Intiializing the buttons with their respective random numbers
                    btArr[i][j].setText(nums[k].toString());//Intiializing the buttons with their respective random numbers
                }


                int finalI = i;
                int finalJ = j;

                btArr[i][j].setOnClickListener(v -> {// Event listener for when the buttons are clicked

                    //Animating my custom buttons
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
                    v.startAnimation(animation);

                    //Initializing current button being clicked and its neighbors
                    Button current = btArr[finalI][finalJ];
                    Button topNeighbor = null;
                    Button botNeighbor = null;
                    Button leftNeighbor = null;
                    Button rightNeighbor = null;

                    if(finalI - 1 >= 0)
                        topNeighbor = btArr[finalI - 1][finalJ];

                    if(finalI + 1 < 3)
                        botNeighbor = btArr[finalI + 1][finalJ];

                    if(finalJ - 1 >= 0)
                        leftNeighbor = btArr[finalI][finalJ - 1];

                    if(finalJ + 1 < 3)
                        rightNeighbor = btArr[finalI][finalJ + 1];

                    if(!isEmpty(current)){//Checks whether the current button is not the empty string

                        // Checks whether any of the the neighbors is the empty string
                        if (isEmpty(topNeighbor) || isEmpty(botNeighbor) || isEmpty(leftNeighbor) || isEmpty(rightNeighbor)) {

                            //Checking which neighbor is the empty square
                            if(isEmpty(topNeighbor)){

                                topNeighbor.setText(current.getText().toString());
                                current.setText("");
                            }

                            if(isEmpty(botNeighbor)){

                                botNeighbor.setText(current.getText().toString());
                                current.setText("");
                            }

                            if(isEmpty(leftNeighbor)){

                                leftNeighbor.setText(current.getText().toString());
                                current.setText("");
                            }

                            if(isEmpty(rightNeighbor)){

                                rightNeighbor.setText(current.getText().toString());
                                current.setText("");
                            }

                        } else {//If none of the neighbors is an empty square then it is an invalid move

                            Toast.makeText(this, "Invalid Move", Toast.LENGTH_SHORT).show();// Creating a toast saying Invalid Move
                        }

                    }else{//If the current button is the empty string then it is also an invalid move

                        Toast.makeText(this, "Invalid Move", Toast.LENGTH_SHORT).show();
                    }

                    // If the empty square is in the final position and the buttons are in ascending order, win the game
                    if(isEmpty(btArr[2][2]) && isAscending(btArr))
                        Toast.makeText(this, "Congratulations!! You Won!", Toast.LENGTH_LONG).show();

                });

                k++;
            }
        }
    }

    public static boolean isAscending(Button[][] btArr){ // Method to check whether the buttons are in ascending order and have won

        int previous = 0; // initializing the lowest possible number

        //iterate through each button in the button grid
        for(Button[] buttons : btArr) {
            for(Button button : buttons) {

                if(!isEmpty(button)) {// If the button is not the empty string

                    //Converting the current button text to an integer and assigning it to the current value
                    int currentVal = Integer.parseInt(button.getText().toString());

                    if(currentVal < previous) // Checks if the current value is smaller then the previous button
                        return false; // not ascending order

                    previous = currentVal; // is ascending and save the current as the new previous

                }
            }
        }

        return true;// ia in ascending order
    }

    //Method to check whether a button is null and is the empty string
    public static boolean isEmpty(Button button){

        return button != null && button.getText().toString().isEmpty();
    }

    //Shuffle the array elements
    public static void shuffleArr(Integer[] arr){

        Random random = new Random();// Creating a random number generator

        //Iterate through the numbers in the array greater than 1
        for(int i = arr.length - 1; i > 0; i--){

            int j = random.nextInt(i + 1);// Capturing the random number generated

            //Swapping the an element at a random index with the index at i
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}