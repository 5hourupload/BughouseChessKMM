//
//  GameManager.swift
//  iosApp
//
//  Created by Alan Shen on 10/26/22.
//  Copyright © 2022 orgName. All rights reserved.
//
import Foundation
import SwiftUI
import shared
final class GameManager: ObservableObject {
    
    @Published var counter: Int = 0
    @Published var board0 = [[Square]]()
    let gms = GameStateManager()
    var currentMoveType: String = ""
    var currentX: Int = -1
    var currentY: Int = -1
    
    
    init(counter: Int)  {
        self.counter = counter
        initBoard(boardNumber: 0)
        updatePieces()
    }
    func initBoard(boardNumber: Int)
    {
        if (boardNumber == 0)
        {
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
            board0.append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
        }
    }
    
    func updatePieces()
    {
        for x in 0...7 {
            for y in 0...7 {
                board0[x][y].piece = (gms.getPositions(boardNumber: 0).get(index: Int32(x))?.get(index: Int32(y)))!
            }
        }
    }
    public func processMove(x:Int, y: Int, boardNumber: Int)
    {
        if (board0[x][y].cosmetic == "dot" || board0[x][y].cosmetic == "yellow")
        {
            gms.performMove(moveType: currentMoveType, x: Int32(currentX), y: Int32(currentY),x1: Int32(x),y1: Int32(y),boardNumber: Int32(boardNumber));
            updatePieces()

            if (currentMoveType == "take" || currentMoveType == "whiteEnP" || currentMoveType == "blackEnP") {
//                updateRosterUI(boardNumber);
            }
//                    pawnCheck(boardNumber);
            if (gms.gameOver) {
//                gameEndProcedures(gms.gameOverSide, gms.gameOverType);
                return;
            }
        }
        else
        {
            let piece = gms.getPositions(boardNumber: Int32(boardNumber)).get(index: Int32(x))?.get(index: Int32(y))
            let moves = piece!.getMoves(positions: gms.getPositions(boardNumber: Int32(boardNumber)), x: Int32(x), y: Int32(y), boardNumber: Int32(boardNumber));
            
            for m in moves
            {
                if (gms.checkIfMoveResultsInCheck(moveType: m.type,x: m.x,y: m.y,x1: m.x1,y1: m.y1,boardNumber: Int32(boardNumber))) {continue}
                
                board0[x][y].cosmetic = "yellow"
                
                currentMoveType = m.type
                currentX = x
                currentY = y

                if (m.type == "take" || m.type == "whiteEnP" || m.type == "blackEnP")
                {
                    board0[Int(m.x1)][Int(m.y1)].cosmetic = "red";
                }
                else
                {
                    board0[Int(m.x1)][Int(m.y1)].cosmetic = "dot"
                }
            }
        }
    }
}
