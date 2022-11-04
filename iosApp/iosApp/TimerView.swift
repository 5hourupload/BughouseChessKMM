//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TimerView: View {
    @State var rotation: Double = 90
  
    var gameManager: GameManager
    @State var backgroundImage: UIImage

    let timer = Timer.publish(every: 0.1, on: .main, in: .common).autoconnect()
    @State var time: Int = 300*1000
    var boardNumber: Int
    var color: Int
    var yellow: UIImage
    var gray: UIImage

    init(gameManager: GameManager, color: String, boardNumber: Int) {
        let squareSize = UIScreen.main.bounds.width / 10

        yellow = UIColor.yellow.image(CGSize(width: squareSize*2, height: squareSize))
        gray = UIColor.gray.image(CGSize(width: squareSize*2, height: squareSize))
        backgroundImage = gray
        
        self.gameManager = gameManager
        self.boardNumber = boardNumber
        if (color == "white")
        {
            self.color = 1

        }
        else
        {
            self.color = 2
        }

    }
    
    var body: some View
    {
        ZStack{
            Image(uiImage: backgroundImage)
            
            Text(String(self.time/100)).onReceive(timer) { input in
                if (gameManager.gms.gameState == GameStateManager.GameState.playing && gameManager.gms.turn.get(index: Int32(boardNumber)) == color)
                {
                    self.time = self.time - 100
                    self.backgroundImage = yellow
                }
                else
                {
                    self.backgroundImage = gray
                }
            }
        }

    }
    
}

