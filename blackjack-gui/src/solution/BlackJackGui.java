package solution;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.image.Image;
import resource.classes.*;

public class BlackJackGui extends Application{
    Canvas canvas = new Canvas(99*5, (123*2)+150);
    Image cardImages =new Image("resources\\images\\cards.png");
    boolean onGoingGame = false;
    Label dealerInfo;
    Button newGameButton;
    Button standButton;
    Button hitButton;
    Deck deck;
    BlackjackHand dealerHand;
    BlackjackHand playerHand;


    public void start(Stage primaryStage){

        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-color: black; -fx-border-width: 5px; " +
                "-fx-background-color: black");
        primaryStage.setResizable(false);
        root.setCenter(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        dealerInfo = new Label("The dealer is ready to play.");
        root.setTop(dealerInfo);
        dealerInfo.setFont(Font.font("",FontWeight.BOLD, 20));
        dealerInfo.setAlignment(Pos.BASELINE_LEFT);
        dealerInfo.setPrefWidth(canvas.getWidth()+10);
        dealerInfo.setMinHeight(80);
        dealerInfo.setStyle("-fx-border-color: black; -fx-border-width: 5px; " +
                "-fx-background-color: gray");

        hitButton = new Button("Hit");
        hitButton.setPrefWidth(canvas.getWidth()/3);
        hitButton.setOnAction(evt -> onClickedHitButton());
        hitButton.setDisable(true);

        standButton = new Button("Stand");
        standButton.setPrefWidth(canvas.getWidth()/3);
        standButton.setOnAction(evt-> onCLickedStandButton());
        standButton.setDisable(true);

        newGameButton = new Button("Start Game");
        newGameButton.setOnAction(evt -> onClickedStartGame());
        newGameButton.setPrefWidth((canvas.getWidth()/3)-10);

        HBox buttonBar = new HBox();
        root.setBottom(buttonBar);
        buttonBar.getChildren().add(hitButton);
        buttonBar.getChildren().add(standButton);
        buttonBar.getChildren().add(newGameButton);
        buttonBar.setSpacing(5);
        buttonBar.setStyle("-fx-border-color: black; -fx-border-width: 5px; " +
                "-fx-background-color: black");

        drawTable();
        primaryStage.show();
    }

    private void onClickedStartGame(){
        drawTable();
        deck = new Deck();
        deck.shuffle();
        dealerHand = new BlackjackHand();
        playerHand = new BlackjackHand();
        newGameButton.setDisable(true);
        hitButton.setDisable(false);
        standButton.setDisable(false);

        //deal initial hand and draw it
        playerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        drawCard(dealerHand.getCard(0),10,20);
        drawCard(null, 99, 20);
        for(int i = 0; i<playerHand.getCardCount(); i++){
            drawCard(playerHand.getCard(i), ((89*i)+10),(int)canvas.getHeight()-133);
        }

        dealerInfo.setText("");

        if(dealerHand.getBlackjackValue() == 21){
            onGoingGame = false;
            dealerInfo.setText("The dealer won with a BlackJack, getting 21 points.");
            drawCard(dealerHand.getCard(1), 99,20);
            for(int i = 0; i<dealerHand.getCardCount(); i++){
                drawCard(dealerHand.getCard(i),((89*i)+10),20);
            }
            newGameButton.setDisable(false);
            hitButton.setDisable(true);
            standButton.setDisable(true);
        }else if(playerHand.getBlackjackValue()==21){
            onGoingGame = false;
            dealerInfo.setText("You won with a BlackJack, getting 21 points. " );
            for(int i = 0; i<dealerHand.getCardCount(); i++){
                drawCard(dealerHand.getCard(i),((89*i)+10),20);
            }
            newGameButton.setDisable(false);
            hitButton.setDisable(true);
            standButton.setDisable(true);
        }else{
            dealerInfo.setText("Would you like to hit or stand?");
        }
    }

    private void onClickedHitButton(){
        playerHand.addCard(deck.dealCard());
        drawCard(playerHand.getCard(playerHand.getCardCount()-1),(89*(playerHand.getCardCount()-1)+10),(int)canvas.getHeight()-133);
        if(playerHand.getBlackjackValue()>21){
            dealerInfo.setText("Your went over 21. Your score was " + playerHand.getBlackjackValue()+
                    ".");
            hitButton.setDisable(true);
            standButton.setDisable(true);
            newGameButton.setDisable(false);
            for(int i = 0; i<dealerHand.getCardCount(); i++){
                drawCard(dealerHand.getCard(i),((89*i)+10),20);
            }
        }else if(playerHand.getCardCount()==5){
            dealerInfo.setText("Your won by having 5 cards and less than 21 points."+
                    "\n Your score was " + playerHand.getBlackjackValue() + ".");
            hitButton.setDisable(true);
            standButton.setDisable(true);
            newGameButton.setDisable(false);
            for(int i = 0; i<dealerHand.getCardCount(); i++){
                drawCard(dealerHand.getCard(i),((89*i)+10),20);
            }

        }else if(dealerHand.getBlackjackValue()<17){
            dealerHand.addCard(deck.dealCard());
            drawCard(null, (89*(dealerHand.getCardCount()-1)+10), 20);
            if(dealerHand.getBlackjackValue()>21){
                dealerInfo.setText("The dealer went over 21. Their score was " + dealerHand.getBlackjackValue()+
                        ".");
                for(int i = 0; i<dealerHand.getCardCount(); i++){
                    drawCard(dealerHand.getCard(i),((89*i)+10),20);
                }
                newGameButton.setDisable(false);
                hitButton.setDisable(true);
                standButton.setDisable(true);
            }
        }
    }

    private void onCLickedStandButton(){
        while(true){
            if(dealerHand.getBlackjackValue()<17) {
                dealerHand.addCard(deck.dealCard());
                drawCard(null, (89 * (dealerHand.getCardCount() - 1) + 10), 20);
                if (dealerHand.getBlackjackValue() > 21) {
                    dealerInfo.setText("You won. The dealer went over 21. Their score was " +
                            dealerHand.getBlackjackValue() + ".");
                    for (int i = 0; i < dealerHand.getCardCount(); i++) {
                        drawCard(dealerHand.getCard(i), ((89 * i) + 10), 20);
                    }
                    newGameButton.setDisable(false);
                    hitButton.setDisable(true);
                    standButton.setDisable(true);
                }
            } else{
                if(dealerHand.getBlackjackValue()>21){
                    dealerInfo.setText("You won. The dealer went over 21. " +
                            "\n Their score was " +
                            dealerHand.getBlackjackValue() + ".");
                } else if(playerHand.getBlackjackValue()>dealerHand.getBlackjackValue()){
                    dealerInfo.setText("You won. Your scores were: \n You="+playerHand.getBlackjackValue()
                            +", dealer=" + dealerHand.getBlackjackValue()+".");
                } else if(playerHand.getBlackjackValue()<dealerHand.getBlackjackValue()){
                    dealerInfo.setText("The dealer won. Your scores were: \n You=="+playerHand.getBlackjackValue()
                            +", dealer=" + dealerHand.getBlackjackValue()+".");
                } else if(playerHand.getBlackjackValue()==dealerHand.getBlackjackValue()){
                    dealerInfo.setText("The dealer wins on ties. Your scores were "+playerHand.getBlackjackValue()
                            + ".");

                }
                for (int i = 0; i < dealerHand.getCardCount(); i++) {
                    drawCard(dealerHand.getCard(i), ((89 * i) + 10), 20);
                }
                newGameButton.setDisable(false);
                hitButton.setDisable(true);
                standButton.setDisable(true);
                break;
            }
        }
    }

    private void drawTable (){
        GraphicsContext graphicsContext =  canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.rgb(40,108,65));
        graphicsContext.fillRect(0,0, canvas.getWidth(),canvas.getHeight());

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setFont(Font.font("", FontWeight.BOLD, 15));
        graphicsContext.fillText("Dealer's Hand:", 10,15);
        graphicsContext.fillText("Your Hand:", 10,canvas.getHeight()-138);

    }

    private void drawCard(Card card, int xCoord, int yCoord) {
        int cardRow, cardCol;
        if (card == null) {
            cardRow = 4;   // row and column of a face down card
            cardCol = 2;
        }
        else {
            cardRow = 3 - card.getSuit();
            cardCol = card.getValue() - 1;
        }
        double sx,sy;  // top left corner of source rect for card in cardImages
        sx = 79 * cardCol;
        sy = 123 * cardRow;
        canvas.getGraphicsContext2D().drawImage(cardImages, sx,sy,79,123, xCoord,yCoord,79,123 );
    } // end drawCard()

    public static void main(String[]Args){
        launch(Args);
    }
}
