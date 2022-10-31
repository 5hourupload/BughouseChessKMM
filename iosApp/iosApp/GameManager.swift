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
    @Published var board = [[[Square]]](repeating: [[Square]](), count: 2)
    @Published var roster0W = [RosterSquare]()
    @Published var roster0B = [RosterSquare]()
    @Published var roster1W = [RosterSquare]()
    @Published var roster1B = [RosterSquare]()
    let gms = GameStateManager()
    
    var moves: Set<Move> = Set()
    
    
    init(counter: Int)  {
        self.counter = counter
        initBoard(boardNumber: 0)
        updatePieces()
    }
    func initBoard(boardNumber: Int)
    {
        if (boardNumber == 0)
        {
            for b in 0...1 {
                for _ in 0...7 {
                    board[b].append([Square(), Square(), Square(), Square(), Square(), Square(), Square(), Square()])
                }
            }
        }
        for _ in 0...30 {
            roster0W.append(RosterSquare())
            roster0B.append(RosterSquare())
            roster1W.append(RosterSquare())
            roster1B.append(RosterSquare())
        }
    }
    
    func updatePieces()
    {
        for b in 0...1 {
            for x in 0...7 {
                for y in 0...7 {
                    board[b][x][y].piece = (gms.getPositions(boardNumber: Int32(b)).get(index: Int32(x))?.get(index: Int32(y)))!
                }
            }
        }
    }
    public func processMove(x:Int, y: Int, boardNumber: Int)
    {
        for m in moves
        {
            if (m.x1 == x && m.y1 == y)
            {
  
                gms.performMove(moveType: m.type, x: m.x, y: m.y, x1: Int32(x),y1: Int32(y), boardNumber: Int32(boardNumber));
                updatePieces()
                

                if (m.type == "take" || m.type == "whiteEnP" || m.type == "blackEnP") {
    //                updateRosterUI(boardNumber);
                }
    //                    pawnCheck(boardNumber);
                clean(boardNumber: boardNumber)

                if (gms.gameOver) {
    //                gameEndProcedures(gms.gameOverSide, gms.gameOverType);
                    return;
                }
                return
            }
        }
        
        
        clean(boardNumber: boardNumber)
        let piece = gms.getPositions(boardNumber: Int32(boardNumber)).get(index: Int32(x))?.get(index: Int32(y))
        moves = piece!.getMoves(positions: gms.getPositions(boardNumber: Int32(boardNumber)), x: Int32(x), y: Int32(y), boardNumber: Int32(boardNumber));

        for m in moves
        {
            if (gms.checkIfMoveResultsInCheck(moveType: m.type,x: m.x,y: m.y,x1: m.x1,y1: m.y1,boardNumber: Int32(boardNumber))) {continue}
            
            board[boardNumber][x][y].cosmetic = "yellow"

            if (m.type == "take" || m.type == "whiteEnP" || m.type == "blackEnP")
            {
                board[boardNumber][Int(m.x1)][Int(m.y1)].cosmetic = "red";
            }
            else
            {
                board[boardNumber][Int(m.x1)][Int(m.y1)].cosmetic = "dot"
            }
        }
        
    }
    func clean(boardNumber: Int)
    {
        for x in 0...7 {
            for y in 0...7 {
                board[boardNumber][x][y].cosmetic = "none"
            }
        }
    }
}
