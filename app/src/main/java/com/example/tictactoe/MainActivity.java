package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.switchmaterial.SwitchMaterial;

import com.example.tictactoe.utils.MyArray;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int BUTTON_COUNT = 16;

    private final MyArray [] wc = {
            new MyArray(), new MyArray(), new MyArray(),
            new MyArray(), new MyArray(), new MyArray(),
            new MyArray(), new MyArray(), new MyArray(),
            new MyArray()
    };

    private final MyArray playerPos = new MyArray();

    private final MyArray opponentPos = new MyArray();
    private boolean isComputerOn = true;
    private boolean isOpponentTurn = false;
    private Button resetButton;
    private SwitchMaterial computerSwitch;
    private TextView computerText;

    private final ArrayList<Button> buttons = new ArrayList<>();

    public void onClick(View clickedButton) {
        setButtonsEnabled(false);
        if (clickedButton == resetButton) {
            resetGame();
            return;
        }
        if (clickedButton == computerSwitch) {
            if (computerSwitch.isChecked()) {
                computerText.setText(R.string.Computer_ON);
                isComputerOn = true;
            }
            else {
                computerText.setText(R.string.Computer_OFF);
                isComputerOn = false;
            }
            resetGame();
            return;
        }
        
        int buttonIndex = 0;
        for (int i = 0; i < BUTTON_COUNT; i++) {
            if (clickedButton == buttons.get(i)) {
                buttonIndex = i;
                break;
            }
        }
        Log.d("TicTac","Pulsado boton " + buttonIndex);
        manageMove(buttonIndex);
    }

    private void manageMove(int buttonIndex) {
        if (isComputerOn) {
            buttons.get(buttonIndex).setText("X");
            playerPos.addNumber(buttonIndex);
            if (checkWinner(playerPos)) {
                showAlert("¡ Enhorabuena, has ganado !");
                return;
            }
            if (!haveFreePositions()) {
                showAlert(" No hay movimientos disponibles ");
                return;
            }
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                executeComputerMove();
                if (checkWinner(opponentPos)) {
                    showAlert(" ¡ Has perdido, LOSER ! ");
                }
                setButtonsEnabled(true);
            }, 700);
        } else {
            if (isOpponentTurn) {
                buttons.get(buttonIndex).setText("O");
                opponentPos.addNumber(buttonIndex);
            } else {
                buttons.get(buttonIndex).setText("X");
                playerPos.addNumber(buttonIndex);
            }
            if (checkWinner(playerPos)) {
                showAlert("¡Ha ganado el jugador X");
            } else if (checkWinner(opponentPos)) {
                showAlert("Ha ganado el jugador O");
            } else if (!haveFreePositions()) {
                showAlert("No hay movimientos disponibles");
            }
            isOpponentTurn = !isOpponentTurn;
            setButtonsEnabled(true);
        }
    }

    private void executeComputerMove() {
        //If AI can win, then win
        for (int i = 0; i < 10; i++) {
            MyArray res = wc[i].substract(opponentPos);
            int move = res.itemAt(0);
            if ( res.count == 1 && !playerPos.hasNumber(move) && !opponentPos.hasNumber(move)) {
                buttons.get(move).setText("O");
                opponentPos.addNumber(move);
                Log.d("TicTacToe", "Computer is winning with move " + move);
                return;
            }
        }
        //If AI can block, then block
        for (int i = 0; i < 10; i++) {
            MyArray res = wc[i].substract(playerPos);
            int move = res.itemAt(0);
            if ( res.count == 1 && !opponentPos.hasNumber(move)) {
                buttons.get(move).setText("O");
                opponentPos.addNumber(move);
                Log.d("TicTacToe", "Computer is blocking with move " + move);
                return;
            }
        }
        //If AI cant win or block, then select center position
        if (!opponentPos.hasNumber(4) && !playerPos.hasNumber(4)) {
            buttons.get(4).setText("O");
            opponentPos.addNumber(4);
            Log.d("TicTacToe", "Computer is moving to center ");
            return;
        }
        //else, select random position
        int computerMove = getFreePosition();
        buttons.get(computerMove).setText("O");
        opponentPos.addNumber(computerMove);
        Log.d("TicTacToe", "Computer is selection random position " + computerMove);
    }

    private int getFreePosition() {
        Random random = new Random();
        int freePosition = random.nextInt(BUTTON_COUNT);
        while(playerPos.hasNumber(freePosition) || opponentPos.hasNumber(freePosition)) {
            freePosition = random.nextInt(BUTTON_COUNT);
        }
        return freePosition;
    }

    private boolean checkWinner(MyArray positions) {
        boolean winRes = false;
        for (int i = 0; i < 10; i++) {
           if (wc[i].isSubset(positions)) {
               winRes = true;
               break;
           }
        }
        return winRes;
    }

    private void resetGame() {
        for (int i = 0; i < BUTTON_COUNT; i++) {
            buttons.get(i).setText("");
        }
        playerPos.clear();
        opponentPos.clear();
        isOpponentTurn = false;
        setButtonsEnabled(true);
    }

    private void setButtonsEnabled(boolean isEnabled) {
        for (int i = 0; i < BUTTON_COUNT; i++) {
            buttons.get(i).setEnabled(isEnabled);
        }
    }

    private boolean haveFreePositions() {
        return playerPos.count + opponentPos.count < BUTTON_COUNT;
    }

    private void prepareViews() {
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        computerSwitch = findViewById(R.id.computerSwitch);
        computerSwitch.setOnClickListener(this);
        computerText = findViewById(R.id.computerTextView);

        int[] buttonsId = {
            R.id.button_0,
            R.id.button_1,
            R.id.button_2,
            R.id.button_3,
            R.id.button_4,
            R.id.button_5,
            R.id.button_6,
            R.id.button_7,
            R.id.button_8,
                R.id.button_9,
                R.id.button_10,
                R.id.button_11,
                R.id.button_12,
                R.id.button_13,
                R.id.button_14,
                R.id.button_15

        };





        for(int i = 0; i < BUTTON_COUNT; i++) {
            buttons.add(findViewById(buttonsId[i]));
        }

        buttons.add(resetButton);
        for (int i = 0; i < BUTTON_COUNT; i++) {
            buttons.get(i).setOnClickListener(this);
            buttons.get(i).setTextSize(40);
            buttons.get(i).setBackgroundColor(getColor(R.color.green));
            buttons.get(i).setTextColor(Color.rgb(0,0,0));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        prepareWC();
        prepareViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void prepareWC() {
        wc[0].addNumber(0);
        wc[0].addNumber(1);
        wc[0].addNumber(2);
        wc[0].addNumber(3);
        wc[1].addNumber(4);
        wc[1].addNumber(5);
        wc[1].addNumber(6);
        wc[1].addNumber(7);
        wc[2].addNumber(8);
        wc[2].addNumber(9);
        wc[2].addNumber(10);
        wc[2].addNumber(11);
        wc[3].addNumber(12);
        wc[3].addNumber(13);
        wc[3].addNumber(14);
        wc[3].addNumber(15);
        wc[4].addNumber(0);
        wc[4].addNumber(4);
        wc[4].addNumber(8);
        wc[4].addNumber(12);
        wc[5].addNumber(1);
        wc[5].addNumber(5);
        wc[5].addNumber(9);
        wc[5].addNumber(13);
        wc[6].addNumber(2);
        wc[6].addNumber(6);
        wc[6].addNumber(10);
        wc[6].addNumber(14);
        wc[7].addNumber(3);
        wc[7].addNumber(7);
        wc[7].addNumber(11);
        wc[7].addNumber(15);
        wc[8].addNumber(0);
        wc[8].addNumber(5);
        wc[8].addNumber(10);
        wc[8].addNumber(15);
        wc[9].addNumber(3);
        wc[9].addNumber(6);
        wc[9].addNumber(9);
        wc[9].addNumber(12);

    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("TicTacToe")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> resetGame())
                .setCancelable(false) // Impide que se cierre al tocar fuera
                .show();
    }
}