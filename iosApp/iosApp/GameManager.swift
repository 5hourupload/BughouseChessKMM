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
    public func potentialMoves(x:Int, y: Int, boardNumber: Int)
    {
        var piece = gms.getPositions(boardNumber: Int32(boardNumber)).get(index: Int32(x))?.get(index: Int32(y))
        var moves = piece!.getMoves(gms.getPositions(boardNumber: Int32(boardNumber)), Int32(x), Int32(y), Int32(boardNumber));
        
    }
}
