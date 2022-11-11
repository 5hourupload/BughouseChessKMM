//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct StartButtonView: View {
    
    var gameManager: GameManager
    @State var text: String = "Start"
    
    init(gameManager: GameManager) {
        self.gameManager = gameManager
        

    }
    
    var body: some View
    {
        let squareSize = UIScreen.main.bounds.width / 10

        Button(text) {
            if (gameManager.gms.gameState == GameStateManager.GameState.pregame)
            {
//                newGame();
//                        scroll1.fullScroll(View.FOCUS_UP);
//                        scroll3.fullScroll(View.FOCUS_UP);
//                        scroll2.fullScroll(View.FOCUS_DOWN);
//                        scroll4.fullScroll(View.FOCUS_DOWN);

                gameManager.start()
//                startTimers();
                text = "Pause"
            }
            else if (gameManager.gms.gameState == GameStateManager.GameState.paused)
            {
                gameManager.gms.resume();
                text = "Pause"
                return;
            }
            else if (gameManager.gms.gameState == GameStateManager.GameState.playing)
            {
                gameManager.clean(boardNumber: 0,leaveCheck: true);
                gameManager.clean(boardNumber: 1, leaveCheck: true);
                gameManager.gms.pause();
                text = "Resume";
            }

                            
        }.frame(width: squareSize * 2, height: squareSize * 0.75).foregroundColor(.black)
            .background(Color.gray)
            .cornerRadius(10)

    }
    
}

