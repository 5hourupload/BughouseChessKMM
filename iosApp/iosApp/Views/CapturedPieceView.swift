//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct CapturedPieceView: View {
    @State var rotation: Double = 90
    var x: Int
    var y: Int
    var boardNumber: Int
    @ObservedObject var gameManager: GameManager
    @State var dragging: Bool = false
    @State private var location: CGPoint = CGPoint(x: 0, y: 0)

    
    init(x: Int, y: Int, boardNumber: Int, gameManager: GameManager) {

        
        self.x = x
        self.y = y
        self.boardNumber = boardNumber
        self.gameManager = gameManager
    }
    
    var body: some View
    {
        let squareSize = UIScreen.main.bounds.width / 10

        ZStack{
            Image(uiImage: gameManager.board[boardNumber][x][y].getUIImage())
                .rotationEffect(.degrees(getRotation()))
                .gesture(simpleDrag)
                .onTapGesture {
                    gameManager.processMove(x: x,y: y,boardNumber: boardNumber)
                }
            if dragging{
                Image(uiImage: gameManager.board[boardNumber][x][y].getUIImage().rotate(radians: Float(getRotationRadions()))!)
                    .position(location)
            }
        }.frame(width: squareSize, height: squareSize)
        


    }
    var simpleDrag: some Gesture {
            DragGesture()
                .onChanged { value in
                    self.location = value.location
                    if !dragging {
                        gameManager.processMove(x: x, y: y, boardNumber: boardNumber)
                    }
                    dragging = true
                }
                .onEnded { value in
                    dragging = false
                    let squareSize = UIScreen.main.bounds.width / 10
                    var newX = x
                    var newY = y
                    if boardNumber == 0
                    {
                        if value.location.x < 0 {
                            newY = y + (Int((value.location.x) / squareSize) - 1)
                        }
                        else {
                            newY = y + Int((value.location.x) / squareSize)
                        }
                        if value.location.y < 0 {
                            newX = x + (Int((value.location.y) / squareSize) - 1)
                        }
                        else {
                            newX = x + Int((value.location.y) / squareSize)
                        }
                    }
                    if boardNumber == 1
                    {
                        if value.location.x < 0 {
                            newY = y - (Int((value.location.x) / squareSize) - 1)
                        }
                        else {
                            newY = y - Int((value.location.x) / squareSize)
                        }
                        if value.location.y < 0 {
                            newX = x - (Int((value.location.y) / squareSize) - 1)
                        }
                        else {
                            newX = x - Int((value.location.y) / squareSize)
                        }
                    }
                    
                    if (newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7)
                    {
                        gameManager.processMove(x: newX, y: newY, boardNumber: boardNumber)

                    }

                }
        }
    private func getRotation() -> Double
    {
        
        if boardNumber == 0
        {
            if gameManager.board[boardNumber][x][y].piece.color == "white" { return 90.0 }
            if gameManager.board[boardNumber][x][y].piece.color == "black" { return 270.0 }
        }
        else
        {
            if gameManager.board[boardNumber][x][y].piece.color == "white" { return 270.0 }
            if gameManager.board[boardNumber][x][y].piece.color == "black" { return 90.0 }
        }
        return 0.0
    }
    private func getRotationRadions() -> Float
    {
        
        if boardNumber == 0
        {
            if gameManager.board[boardNumber][x][y].piece.color == "white" { return .pi/2 }
            if gameManager.board[boardNumber][x][y].piece.color == "black" { return .pi*3/2}
        }
        else
        {
            if gameManager.board[boardNumber][x][y].piece.color == "white" { return .pi*3/2 }
            if gameManager.board[boardNumber][x][y].piece.color == "black" { return .pi/2 }
        }
        return 0.0
    }
    
    
}

