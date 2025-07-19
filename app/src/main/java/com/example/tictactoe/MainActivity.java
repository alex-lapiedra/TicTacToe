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
    private final MyArray [] wc = {
            new MyArray(), new MyArray(), new MyArray(),
            new MyArray(), new MyArray(), new MyArray(),
            new MyArray(), new MyArray()
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
        for (int i = 0; i < 9; i++) {
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
                checkWinner(opponentPos);
                if (checkWinner(opponentPos)) {
                    showAlert(" ¡ Has perdido, LOSER ! ");
                }
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
        }
    }

    private void executeComputerMove() {
        //If AI can win, then win
        for (int i = 0; i < 8; i++) {
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
        for (int i = 0; i < 8; i++) {
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
        int freePosition = random.nextInt(9);
        while(playerPos.hasNumber(freePosition) || opponentPos.hasNumber(freePosition)) {
            freePosition = random.nextInt(9);
        }
        return freePosition;
    }

    private boolean checkWinner(MyArray positions) {
        boolean winRes = false;
        for (int i = 0; i < 8; i++) {
           if (wc[i].isSubset(positions)) {
               winRes = true;
               break;
           }
        }
        return winRes;
    }

    private void resetGame() {
        for (int i = 0; i < 9; i++) {
            buttons.get(i).setText("");
        }
        playerPos.clear();
        opponentPos.clear();
        isOpponentTurn = false;
    }

    private boolean haveFreePositions() {
        return playerPos.count + opponentPos.count < 9;
    }

    private void prepareViews() {
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        computerSwitch = findViewById(R.id.computerSwitch);
        computerText = findViewById(R.id.computerTextView);
        computerSwitch.setOnClickListener(this);
        Button button_0 = findViewById(R.id.button_0);
        Button button_1 = findViewById(R.id.button_1);
        Button button_2 = findViewById(R.id.button_2);
        Button button_3 = findViewById(R.id.button_3);
        Button button_4 = findViewById(R.id.button_4);
        Button button_5 = findViewById(R.id.button_5);
        Button button_6 = findViewById(R.id.button_6);
        Button button_7 = findViewById(R.id.button_7);
        Button button_8 = findViewById(R.id.button_8);
        buttons.add(button_0);
        buttons.add(button_1);
        buttons.add(button_2);
        buttons.add(button_3);
        buttons.add(button_4);
        buttons.add(button_5);
        buttons.add(button_6);
        buttons.add(button_7);
        buttons.add(button_8);
        buttons.add(resetButton);
        for (int i = 0; i < 9; i++) {
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
        wc[1].addNumber(3);
        wc[1].addNumber(4);
        wc[1].addNumber(5);
        wc[2].addNumber(6);
        wc[2].addNumber(7);
        wc[2].addNumber(8);
        wc[3].addNumber(0);
        wc[3].addNumber(3);
        wc[3].addNumber(6);
        wc[4].addNumber(1);
        wc[4].addNumber(4);
        wc[4].addNumber(7);
        wc[5].addNumber(2);
        wc[5].addNumber(5);
        wc[5].addNumber(8);
        wc[6].addNumber(0);
        wc[6].addNumber(4);
        wc[6].addNumber(8);
        wc[7].addNumber(2);
        wc[7].addNumber(4);
        wc[7].addNumber(6);
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