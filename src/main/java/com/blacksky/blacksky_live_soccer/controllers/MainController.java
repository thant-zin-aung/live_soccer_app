package com.blacksky.blacksky_live_soccer.controllers;

import com.blacksky.blacksky_live_soccer.Main;
import com.blacksky.blacksky_live_soccer.models.AnimationStyles;
import com.blacksky.blacksky_live_soccer.models.SportApiManipulator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Text apiStatus;
    @FXML
    private VBox premierLeagueWrapper,bundesligaWrapper,serieAWrapper,laLigaWrapper,
            ligue1Wrapper,uefaChampionLeagueWrapper,uefaEuropaLeagueWrapper,englandChampionshipWrapper,
            englandFaCupWrapper,euroChampionshipWrapper,worldCupWrapper;
    @FXML
    private AnchorPane homeInfoWrapper,premierLeagueInfoWrapper,bundesligaInfoWrapper,serieAInfoWrapper,laLigaInfoWrapper,ligue1InfoWrapper,
                            uefaChampionLeagueInfoWrapper,uefaEuropaLeagueInfoWrapper,englandChampionshipInfoWrapper,englandFaCupInfoWrapper,
                            euroChampionshipInfoWrapper,worldCupInfoWrapper;
    @FXML
    private FlowPane flowPane;
    @FXML
    private HBox waitingBox;
    @FXML
    private VBox playButtonWrapper;
    @FXML
    private VBox efhdButton,ehdButton,esdButton,nefhdButton,nehdButton,nesdButton;
    private final ObservableList<VBox> homeFlowPaneItems = FXCollections.observableArrayList();
    private final ObservableList<VBox> matchesFLowPaneItems = FXCollections.observableArrayList();
    private final ObservableList<AnchorPane> infoWrappers = FXCollections.observableArrayList();
    private final Map<VBox,Integer> apiLeagueIdMap = new HashMap<>();
    private final double leagueWrapperScaleSize = 1.1;
    private final double leagueWrapperScaleSpeed = 200;

    private Transition down;
    private Transition up;

    private final int premierLeagueId = 39;
    private final int bundesligaId = 78;
    private final int serieaId = 135;
    private final int laligaId = 140;
    private final int ligue1Id = 61;
    private final int uefaChampionId = 2;
    private final int uefaEuropaId = 3;
    private final int englandChampionShipId = 40;
    private final int englandFaId = 45;
    private final int euroChampionShipId = 4;
    private final int worldCupId = 1;

    private SportApiManipulator sportApiManipulator;
    public void initialize() {
        sportApiManipulator = new SportApiManipulator();
        infoWrappers.addAll(homeInfoWrapper,premierLeagueInfoWrapper,bundesligaInfoWrapper,serieAInfoWrapper,laLigaInfoWrapper,ligue1InfoWrapper,
                            uefaChampionLeagueInfoWrapper,uefaEuropaLeagueInfoWrapper,englandChampionshipInfoWrapper,englandFaCupInfoWrapper,
                            euroChampionshipInfoWrapper,worldCupInfoWrapper);
        removeFlowPaneItem(waitingBox);
        removeFlowPaneItem(playButtonWrapper);
        updateApiStatus();
        mapApiLeagueIds();
        addHomeFlowPaneItems();
        hideInfoWrappersFromScreen(homeInfoWrapper);

        this.down = new Transition() {
            {
                setCycleDuration(Duration.INDEFINITE);
            }
            @Override
            protected void interpolate(double v) {
                scrollPane.setVvalue(scrollPane.getVvalue()+0.03);
            }
        };

        this.up = new Transition() {
            {
                setCycleDuration(Duration.INDEFINITE);
            }
            @Override
            protected void interpolate(double v) {
                scrollPane.setVvalue(scrollPane.getVvalue()-0.03);
            }
        };
    }

    @FXML
    private void handleKeyPress(KeyEvent event){
        if(event.getCode() == KeyCode.S){
            down.play();
        }
        if(event.getCode() == KeyCode.W){
            up.play();
        }
    }

    @FXML
    private void handleKeyRelease(){
        this.down.stop();
        this.up.stop();
    }

    private void mapApiLeagueIds() {
        apiLeagueIdMap.put(premierLeagueWrapper,premierLeagueId);
        apiLeagueIdMap.put(bundesligaWrapper,bundesligaId);
        apiLeagueIdMap.put(serieAWrapper,serieaId);
        apiLeagueIdMap.put(laLigaWrapper,laligaId);
        apiLeagueIdMap.put(ligue1Wrapper,ligue1Id);
        apiLeagueIdMap.put(uefaChampionLeagueWrapper,uefaChampionId);
        apiLeagueIdMap.put(uefaEuropaLeagueWrapper,uefaEuropaId);
        apiLeagueIdMap.put(englandChampionshipWrapper,englandChampionShipId);
        apiLeagueIdMap.put(englandFaCupWrapper,englandFaId);
        apiLeagueIdMap.put(euroChampionshipWrapper,euroChampionShipId);
        apiLeagueIdMap.put(worldCupWrapper,worldCupId);
    }

    private void addHomeFlowPaneItems() {
        flowPane.getChildren().forEach(item->homeFlowPaneItems.add((VBox) item));
    }

    private void hideInfoWrappersFromScreen(AnchorPane exception) {
        infoWrappers.forEach(wrapper->{
            if ( wrapper!=exception ) {
                wrapper.setVisible(false);
                wrapper.setDisable(true);
                wrapper.setManaged(false);
            }
        });
    }
    private void unHideInfoWrapper(AnchorPane wrapper) {
        wrapper.setVisible(true);
        wrapper.setDisable(false);
        wrapper.setManaged(true);
    }

    private void updateApiStatus() {
        apiStatus.setText("Sport API usage in percent: "+String.valueOf(sportApiManipulator.getApiStatusPercent())+"%");
    }


    @FXML
    private void hoverOnLeagueWrapper(MouseEvent event) {
        VBox hoveredLeagueWrapper = (VBox) event.getSource();
        AnimationStyles.scaleEffect(hoveredLeagueWrapper,leagueWrapperScaleSpeed,1,false,1,1,leagueWrapperScaleSize,leagueWrapperScaleSize);
    }

    @FXML
    private void hoverExitOnLeagueWrapper(MouseEvent event) {
        VBox hoveredLeagueWrapper = (VBox) event.getSource();
        AnimationStyles.scaleEffect(hoveredLeagueWrapper,leagueWrapperScaleSpeed,1,false,leagueWrapperScaleSize,leagueWrapperScaleSize,1,1);
    }

    @FXML
    private void clickOnSeeMatchesButton(MouseEvent event) {
        Button seeMatchesButton = (Button) event.getSource();
        HBox seeMatchesButtonWrapper = (HBox) seeMatchesButton.getParent();
        VBox currentLeagueWrapper = (VBox) seeMatchesButtonWrapper.getParent();
        sceneSwitcher(currentLeagueWrapper);
    }

    @FXML
    private void hoverOnPlayButton(MouseEvent event) {
        VBox hoveredButton = (VBox) event.getSource();
        AnimationStyles.scaleEffect(hoveredButton,leagueWrapperScaleSpeed,1,false,1,1,leagueWrapperScaleSize,leagueWrapperScaleSize);
    }

    @FXML
    private void hoverExitOnPlayButton(MouseEvent event) {
        VBox hoveredButton = (VBox) event.getSource();
        AnimationStyles.scaleEffect(hoveredButton,leagueWrapperScaleSpeed,1,false,leagueWrapperScaleSize,leagueWrapperScaleSize,1,1);
    }

    @FXML
    private void clickOnPlayButton(MouseEvent event) {
        VBox clickedButton = (VBox) event.getSource();
        if ( clickedButton == efhdButton ) {

        } else if ( clickedButton == ehdButton ) {

        } else if ( clickedButton == esdButton ) {

        } else if ( clickedButton == nefhdButton ) {

        } else if ( clickedButton == nehdButton ) {

        } else if ( clickedButton == nesdButton ) {

        }
    }

    private void sceneSwitcher(VBox currentLeagueWrapper) {
        AnchorPane currentInfoWrapper = null;
        if ( currentLeagueWrapper == premierLeagueWrapper ) {
            currentInfoWrapper = premierLeagueInfoWrapper;
            sportApiManipulator.setLeagueName("Premier League");
        } else if ( currentLeagueWrapper == bundesligaWrapper ) {
            currentInfoWrapper = bundesligaInfoWrapper;
            sportApiManipulator.setLeagueName("Bundesliga");
        } else if ( currentLeagueWrapper == serieAWrapper ) {
            currentInfoWrapper = serieAInfoWrapper;
            sportApiManipulator.setLeagueName("Serie A");
        } else if ( currentLeagueWrapper == laLigaWrapper ) {
            currentInfoWrapper = laLigaInfoWrapper;
            sportApiManipulator.setLeagueName("La Liga");
        } else if ( currentLeagueWrapper == ligue1Wrapper ) {
            currentInfoWrapper = ligue1InfoWrapper;
            sportApiManipulator.setLeagueName("Ligue 1");
        } else if ( currentLeagueWrapper == uefaChampionLeagueWrapper ) {
            currentInfoWrapper = uefaChampionLeagueInfoWrapper;
            sportApiManipulator.setLeagueName("UEFA Champion League");
        } else if ( currentLeagueWrapper == uefaEuropaLeagueWrapper ) {
            currentInfoWrapper = uefaEuropaLeagueInfoWrapper;
            sportApiManipulator.setLeagueName("UEFA Europa League");
        } else if ( currentLeagueWrapper == englandChampionshipWrapper ) {
            currentInfoWrapper = englandChampionshipInfoWrapper;
            sportApiManipulator.setLeagueName("England Championship");
        } else if ( currentLeagueWrapper == englandFaCupWrapper ) {
            currentInfoWrapper = englandFaCupInfoWrapper;
            sportApiManipulator.setLeagueName("England FA Cup");
        } else if ( currentLeagueWrapper == euroChampionshipWrapper ) {
            currentInfoWrapper = euroChampionshipInfoWrapper;
            sportApiManipulator.setLeagueName("Euro Championship");
        } else if ( currentLeagueWrapper == worldCupWrapper ) {
            currentInfoWrapper = worldCupInfoWrapper;
            sportApiManipulator.setLeagueName("World Cup");
        }

        sportApiManipulator.setSelectedLeagueApiId(apiLeagueIdMap.get(currentLeagueWrapper));
        removeItemsFromFlowPane(homeFlowPaneItems);
        assert currentInfoWrapper != null;
        hideInfoWrappersFromScreen(currentInfoWrapper);
        unHideInfoWrapper(currentInfoWrapper);
        scrollPane.setVvalue(0);
        syncFixtureDataToFlowPane();
    }

    private void removeItemsFromFlowPane(ObservableList<VBox> itemList) {
        flowPane.getChildren().removeAll(itemList);
    }

    private void removeFlowPaneItem(Node node) {
        flowPane.getChildren().remove(node);
    }

    private void addItemToFlowPane(Node node) {
        flowPane.getChildren().add(node);
    }

    private void syncFixtureDataToFlowPane() {
        addItemToFlowPane(waitingBox);
        new Thread(()->{
            sportApiManipulator.fetchListOfFixtureData();
            int totalMatch = sportApiManipulator.getTotalFixture();
            for ( int count = 0 ; count < totalMatch ; count++ ) {
                Map<String,String> fixtureData = sportApiManipulator.getFixtureDataFromArray(count);
                String leagueName = sportApiManipulator.getLeagueName();
                int fixtureId = Integer.parseInt(fixtureData.get("fixtureId"));
                int homeTeamId = Integer.parseInt(fixtureData.get("homeTeamId"));
                int awayTeamId = Integer.parseInt(fixtureData.get("awayTeamId"));
                String homeTeamName = fixtureData.get("homeTeamName");
                String awayTeamName = fixtureData.get("awayTeamName");
                String matchShortStatus = fixtureData.get("matchShortStatus");
                String matchDate = fixtureData.get("matchDate");
                String matchTime = fixtureData.get("matchTime");
                Platform.runLater(()->{
                    addItemToFlowPane(createMatchInfoWrapper(fixtureId,leagueName,homeTeamId,homeTeamName,awayTeamId,awayTeamName,matchDate,matchTime,matchShortStatus));
                });
            }
            Platform.runLater(()->removeFlowPaneItem(waitingBox));
            updateApiStatus();
        }).start();
    }

    private VBox createMatchInfoWrapper(int fixtureId,String leagueName,int homeTeamId,String homeTeamName,int awayTeamId,String awayTeamName,String matchDate,String matchTime,String matchShortStatus) {
        double matchWrapperWidth = 420;
        double matchWrapperHeight = 220;
        double teamLogoSize = 55;
        VBox root = new VBox();
        root.getStyleClass().add("match-wrapper");
        root.setStyle("    -fx-background-color: white;\n" +
                "    -fx-border-color: white;\n" +
                "    -fx-border-style: solid;\n" +
                "    -fx-border-width: 1px;\n" +
                "    -fx-padding: 0 7px 7px 7px;\n" +
                "    -fx-border-radius: 7px;\n" +
                "    -fx-background-radius: 7px;\n" +
                "    -fx-effect: dropshadow( three-pass-box , #adadad , 10, 0 , 0, 0);");
        root.setPrefWidth(matchWrapperWidth);
        root.setPrefHeight(matchWrapperHeight);
        root.setSpacing(10);
        root.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                root.setStyle("    -fx-background-color: white;\n" +
                        "    -fx-border-color: #e90052;\n" +
                        "    -fx-border-style: solid;\n" +
                        "    -fx-border-width: 1px;\n" +
                        "    -fx-padding: 0 7px 7px 7px;\n" +
                        "    -fx-border-radius: 7px;\n" +
                        "    -fx-background-radius: 7px;\n" +
                        "    -fx-effect: dropshadow( three-pass-box , #38003c , 10, 0 , 0, 0);");
                AnimationStyles.scaleEffect(root,leagueWrapperScaleSpeed,1,false,1,1,leagueWrapperScaleSize,leagueWrapperScaleSize);
            }
        });
        root.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                root.setStyle("    -fx-background-color: white;\n" +
                        "    -fx-border-color: white;\n" +
                        "    -fx-border-style: solid;\n" +
                        "    -fx-border-width: 1px;\n" +
                        "    -fx-padding: 0 7px 7px 7px;\n" +
                        "    -fx-border-radius: 7px;\n" +
                        "    -fx-background-radius: 7px;\n" +
                        "    -fx-effect: dropshadow( three-pass-box , #adadad , 10, 0 , 0, 0);");
                AnimationStyles.scaleEffect(root,leagueWrapperScaleSpeed,1,false,leagueWrapperScaleSize,leagueWrapperScaleSize,1,1);
            }
        });


        // top layer...
        HBox leagueWrapper = new HBox();
        leagueWrapper.setAlignment(Pos.TOP_CENTER);
        leagueWrapper.setFillHeight(false);
        leagueWrapper.setPrefWidth(matchWrapperWidth);
        leagueWrapper.setStyle("-fx-background-color: transparent");
        HBox league = new HBox();
        league.setStyle("    -fx-background-color: E6E6E6;\n" +
                "    -fx-background-radius: 0 0 6px 6px;\n" +
                "    -fx-pref-width: 170px;\n" +
                "    -fx-pref-height: 17px;\n");
        league.setAlignment(Pos.CENTER);
        Label leagueLabel = new Label(leagueName);
        leagueLabel.getStyleClass().add("league-label");
        leagueLabel.setStyle("-fx-font-size: 11px; -fx-font-family: 'Roboto', sans-serif;");
        league.getChildren().add(leagueLabel);
        leagueWrapper.getChildren().add(league);

        HBox liveBadgeWrapper = new HBox();
        liveBadgeWrapper.setAlignment(Pos.CENTER_RIGHT);
        liveBadgeWrapper.setFillHeight(false);
        liveBadgeWrapper.setPrefWidth(matchWrapperWidth);
        liveBadgeWrapper.setPrefHeight(25);
        liveBadgeWrapper.setStyle("-fx-background-color: transparent;");
        HBox liveBadge = new HBox();
//        liveBadge.setFillHeight(false);
        liveBadge.setStyle("    -fx-pref-width: 60px;\n" +
                "    -fx-pref-height: 17px;\n" +
                "    -fx-background-radius: 15px;\n" +
                "    -fx-background-color: red;\n" +
                "    -fx-padding: 0;");
        liveBadge.setAlignment(Pos.CENTER);
        liveBadge.setSpacing(5);
        Circle liveDot = new Circle();
        liveDot.setFill(Color.WHITE);
        liveDot.setRadius(4);
        Label liveLabel = new Label("LIVE");
        liveLabel.setTextFill(Color.WHITE);
        liveLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Roboto', sans-serif;");
        liveBadge.getChildren().addAll(liveDot,liveLabel);
        liveBadgeWrapper.getChildren().add(liveBadge);

        StackPane topBarWrapper = new StackPane();
        if ( sportApiManipulator.isMatchLive(matchShortStatus) ) {
            topBarWrapper.getChildren().addAll(leagueWrapper,liveBadgeWrapper);
        } else {
            topBarWrapper.getChildren().add(leagueWrapper);
        }

        // Middle layer

        HBox matchDetailWrapper = new HBox();
        matchDetailWrapper.setAlignment(Pos.CENTER);
        matchDetailWrapper.setSpacing(30);
        matchDetailWrapper.setPrefHeight(120);
//        matchDetailWrapper.setStyle("-fx-border-color: black; -fx-border-style: solid");

        VBox homeTeamWrapper = new VBox();
        homeTeamWrapper.setAlignment(Pos.CENTER);
        homeTeamWrapper.setPrefWidth(100);
//        homeTeamWrapper.setStyle("-fx-border-color: black; -fx-border-style: solid");
        ImageView homeTeamImageView = new ImageView(Main.class.getResource("images/team_logos/"+homeTeamId+".png").toString());
        homeTeamImageView.setFitWidth(teamLogoSize);
        homeTeamImageView.setFitHeight(teamLogoSize);
        homeTeamImageView.setPreserveRatio(true);
        homeTeamImageView.setSmooth(true);
        Label homeTeamNameLabel = new Label(homeTeamName);
        homeTeamNameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: 'Roboto', sans-serif; -fx-text-alignment: center; -fx-padding: 7px 0 0 0;");
        homeTeamNameLabel.setAlignment(Pos.CENTER);
        homeTeamNameLabel.setWrapText(true);
        homeTeamNameLabel.setPrefWidth(100);
        homeTeamNameLabel.setMaxWidth(100);
        homeTeamWrapper.getChildren().addAll(homeTeamImageView,homeTeamNameLabel);

        VBox dateTimeWrapper = new VBox();
        dateTimeWrapper.setAlignment(Pos.CENTER);
//        dateTimeWrapper.setStyle("-fx-border-color: black; -fx-border-style: solid");
        Label timeLabel = new Label(matchTime);
        Label dateLabel = new Label(matchDate);
        timeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Roboto', sans-serif; -fx-text-alignment: center; -fx-padding: 0 0 5px 0");
        dateLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Roboto', sans-serif; -fx-text-alignment: center;");
        dateTimeWrapper.getChildren().addAll(timeLabel,dateLabel);

        VBox awayTeamWrapper = new VBox();
        awayTeamWrapper.setAlignment(Pos.CENTER);
        awayTeamWrapper.setPrefWidth(100);
//        awayTeamWrapper.setStyle("-fx-border-color: black; -fx-border-style: solid");
        ImageView awayTeamImageView = new ImageView(Main.class.getResource("images/team_logos/"+awayTeamId+".png").toString());
        awayTeamImageView.setFitWidth(teamLogoSize);
        awayTeamImageView.setFitHeight(teamLogoSize);
        awayTeamImageView.setPreserveRatio(true);
        awayTeamImageView.setSmooth(true);
        Label awayTeamNameLabel = new Label(awayTeamName);
        awayTeamNameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: 'Roboto', sans-serif; -fx-text-alignment: center; -fx-padding: 7px 0 0 0;");
        awayTeamNameLabel.setAlignment(Pos.CENTER);
        awayTeamNameLabel.setWrapText(true);
        awayTeamNameLabel.setPrefWidth(100);
        awayTeamNameLabel.setMaxWidth(100);
        awayTeamWrapper.getChildren().addAll(awayTeamImageView,awayTeamNameLabel);

        matchDetailWrapper.getChildren().addAll(homeTeamWrapper,dateTimeWrapper,awayTeamWrapper);

        // Bottom layer
        Button enterMatchButton = new Button("Enter The Match");
        enterMatchButton.setPrefWidth(matchWrapperWidth);
        enterMatchButton.setPrefHeight(40);
        enterMatchButton.setStyle("-fx-font-family: 'Roboto', sans-serif; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-background-color: linear-gradient(to right,#e90052,#38003c); -fx-text-fill: white; -fx-cursor: hand;");

        enterMatchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                flowPane.getChildren().forEach(item->matchesFLowPaneItems.add((VBox) item));
                removeItemsFromFlowPane(matchesFLowPaneItems);
                sportApiManipulator.setSelectedFixtureApiId(fixtureId);
            }
        });

        root.getChildren().addAll(topBarWrapper,matchDetailWrapper,enterMatchButton);
        return root;
    }


}