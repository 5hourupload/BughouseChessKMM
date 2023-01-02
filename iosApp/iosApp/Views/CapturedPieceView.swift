//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct CapturedPieceView: View {
    
    var boardNumber: Int = 0
    @ObservedObject var gameManager: GameManager
    @State var dragging: Bool = false
    @State private var location: CGPoint = CGPoint(x: 0, y: 0)
    @State var square: RosterSquare

    init(boardNumber: Int, gameManager: GameManager, square: RosterSquare) {
        self.boardNumber = boardNumber
        self.gameManager = gameManager
        self.square = square
    }
    
    var body: some View
    {
        let squareSize = getSquareSizeGlobal()

        ZStack{
            if square.quantity >= 1
            {
                Image(uiImage: square.getUIImage())
                    .rotationEffect(.degrees(getRotation()))
                    .gesture(simpleDrag)
                    .onTapGesture {
                        gameManager.processRosterClick(square: square, boardNumber: boardNumber)
                    }
                if dragging{
                    Image(uiImage: square.getUIImage().rotate(radians: Float(getRotationRadions()))!)
                        .position(location)
                }
            }
            
        }.frame(width: squareSize, height: squareSize)
        
    }
    var simpleDrag: some Gesture {
            DragGesture()
                .onChanged { value in
                    self.location = value.location
                    if !dragging {
                        gameManager.processRosterClick(square: square, boardNumber: boardNumber)
                    }
                    dragging = true
                }
                .onEnded { value in
                    dragging = false
                    let squareSize = getSquareSizeGlobal()
                    var newX = 0
                    var newY = 0
                    if (boardNumber == 0 && square.color == "white")
                    {
                        newY = Int((value.location.x) / squareSize) - 1
                        if square.pieceType == "pawn" { newX = Int((value.location.y + 1.5 * squareSize) / squareSize) }
                        if square.pieceType == "knight" { newX = Int((value.location.y + 2.5 * squareSize) / squareSize) }
                        if square.pieceType == "bishop" { newX = Int((value.location.y + 3.5 * squareSize) / squareSize) }
                        if square.pieceType == "rook" { newX = Int((value.location.y + 4.5 * squareSize) / squareSize) }
                        if square.pieceType == "queen" { newX = Int((value.location.y + 5.5 * squareSize) / squareSize) }

                    }
                    else if (boardNumber == 0 && square.color == "black")
                    {
                        newY = Int((value.location.x) / squareSize) + 7
                        if square.pieceType == "pawn" { newX = Int((value.location.y + 5.5 * squareSize) / squareSize) }
                        if square.pieceType == "knight" { newX = Int((value.location.y + 4.5 * squareSize) / squareSize) }
                        if square.pieceType == "bishop" { newX = Int((value.location.y + 3.5 * squareSize) / squareSize) }
                        if square.pieceType == "rook" { newX = Int((value.location.y + 2.5 * squareSize) / squareSize) }
                        if square.pieceType == "queen" { newX = Int((value.location.y + 1.5 * squareSize) / squareSize) }

                    }
                    else if (boardNumber == 1 && square.color == "black")
                    {
                        newY = Int((value.location.x) / squareSize) * -1 + 8
                        if square.pieceType == "pawn" { newX = Int((value.location.y * -1 + 6.5 * squareSize) / squareSize) }
                        if square.pieceType == "knight" { newX = Int((value.location.y * -1 + 5.5 * squareSize) / squareSize) }
                        if square.pieceType == "bishop" { newX = Int((value.location.y * -1 + 4.5 * squareSize) / squareSize) }
                        if square.pieceType == "rook" { newX = Int((value.location.y * -1 + 3.5 * squareSize) / squareSize) }
                        if square.pieceType == "queen" { newX = Int((value.location.y * -1 + 2.5 * squareSize) / squareSize) }

                    }
                    else if (boardNumber == 1 && square.color == "white")
                    {
                        newY = Int((value.location.x) / squareSize) * -1
                        if square.pieceType == "pawn" { newX = Int((value.location.y * -1 + 2.5 * squareSize) / squareSize) }
                        if square.pieceType == "knight" { newX = Int((value.location.y * -1 + 3.5 * squareSize) / squareSize) }
                        if square.pieceType == "bishop" { newX = Int((value.location.y * -1 + 4.5 * squareSize) / squareSize) }
                        if square.pieceType == "rook" { newX = Int((value.location.y  * -1 + 5.5 * squareSize) / squareSize) }
                        if square.pieceType == "queen" { newX = Int((value.location.y * -1 + 6.5 * squareSize) / squareSize) }

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
            if square.color == "white" { return 90.0 }
            if square.color == "black" { return 270.0 }
        }
        else
        {
            if square.color == "white" { return 270.0 }
            if square.color == "black" { return 90.0 }
        }
        return 0.0
    }
    private func getRotationRadions() -> Float
    {

        if boardNumber == 0
        {
            if square.color == "white" { return .pi/2 }
            if square.color == "black" { return .pi*3/2}
        }
        else
        {
            if square.color == "white" { return .pi*3/2 }
            if square.color == "black" { return .pi/2 }
        }
        return 0.0
    }
    
}

