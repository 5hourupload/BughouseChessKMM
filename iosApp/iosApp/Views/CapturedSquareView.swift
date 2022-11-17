//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct CapturedSquareView: View {

    var boardNumber: Int
    @ObservedObject var gameManager: GameManager
    @State var square: RosterSquare

    var backgroundImage: UIImage

    
    init(boardNumber: Int, gameManager: GameManager, square: RosterSquare) {

        self.boardNumber = boardNumber
        self.gameManager = gameManager
        self.square = square
        let squareSize = UIScreen.main.bounds.width / 10

        
        if (square.pieceType == "pawn" || square.pieceType == "bishop" || square.pieceType == "queen") {
            backgroundImage = UIColor(hex: "#B0B0B080")!.image(CGSize(width: squareSize, height: squareSize))
        }
        else {
            backgroundImage = UIColor(hex: "#8E8E8E80")!.image(CGSize(width: squareSize, height: squareSize))
        }
    }
    
    var body: some View
    {
        ZStack{
            Image(uiImage: backgroundImage)
            Image(uiImage: square.getCosmetic())
            Text("0")
        }.rotationEffect(.degrees(getRotation()))
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
}
