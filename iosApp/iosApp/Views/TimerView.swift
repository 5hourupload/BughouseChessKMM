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
  
    var gameManager: GameManager
    @State var backgroundImage: UIImage

    let timer = Timer.publish(every: 0.1, on: .main, in: .default)
    var boardNumber: Int
    var color: Int
    var yellow: UIImage
    var gray: UIImage
    @State var active = false
    var timeIndex = -1

    init(gameManager: GameManager, color: String, boardNumber: Int) {
        let squareSize = UIScreen.main.bounds.width / 10

        yellow = UIColor(hex: "#FFC900FF")!.image(CGSize(width: squareSize*2, height: squareSize))
        gray = UIColor(hex: "#B0B0B000")!.image(CGSize(width: squareSize*2, height: squareSize))
        backgroundImage = gray
        
        self.gameManager = gameManager
        self.boardNumber = boardNumber
        if color == "white"
        {
            self.color = 1

        }
        else
        {
            self.color = 2
        }
        
        if boardNumber == 0 && color == "white" { timeIndex = 0 }
        if boardNumber == 0 && color == "black" { timeIndex = 1 }
        if boardNumber == 1 && color == "black" { timeIndex = 2 }
        if boardNumber == 1 && color == "white" { timeIndex = 3 }
        
        _ = timer.connect()

    }
    
    var body: some View
    {
        ZStack{
            Image(uiImage: backgroundImage)
            
            Text(getTimeString(time: gameManager.times[timeIndex])).onReceive(timer) { input in

                self.active = gameManager.gms.gameState == GameStateManager.GameState.playing && gameManager.gms.turn.get(index: Int32(boardNumber)) == color
                if self.active
                {
                    gameManager.times[timeIndex] = gameManager.times[timeIndex] - 100
                    self.backgroundImage = yellow
                }
                else
                {
                    self.backgroundImage = gray
                }
                
                if gameManager.times[timeIndex] <= 0 {
                    var side: Int = -1
                    if boardNumber == 0 && color == 1 { side = 1 }
                    if boardNumber == 0 && color == 2 { side = 0 }
                    if boardNumber == 1 && color == 1 { side = 0 }
                    if boardNumber == 1 && color == 2 { side = 1 }
                    gameManager.gameEndProcedures(side: Int32(side), type: 2);

                }
            }.rotationEffect(.degrees(getRotation())).foregroundColor(self.active ? .white : .gray).font(.system(size: 30, weight: .bold))
        }

    }
    
    private func getRotation() -> Double
    {
        
        if boardNumber == 0
        {
            return 0.0
        }
        else
        {
            return 180.0
        }
    }
    
    private func getTimeString(time: Int) -> String {
        let seconds = time / 1000
        let min = Int(seconds / 60)
        let sec = seconds % 60
        return String(min) + ":" + String(format: "%02d",sec)
    }
}

