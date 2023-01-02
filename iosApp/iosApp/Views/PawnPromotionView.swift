//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct PawnPromotionView: View {
    
    var boardNumber: Int
    var gameManager: GameManager
    @State var show: Bool

    
    init(boardNumber: Int, gameManager: GameManager, show: Bool) {
        
        self.boardNumber = boardNumber
        self.gameManager = gameManager
        self.show = show


    }
    
    var body: some View
    {
        let squareSize = getSquareSizeGlobal()

        if gameManager.showPawnOptions[boardNumber] {
            VStack(spacing: 10)
            {
                Button("Queen") {
                    gameManager.selectPromotionPiece(piece: "queen", boardNumber: boardNumber)
                }
                .frame(width: squareSize*8, height: squareSize * 1.5)
                .background(Color.init(hex: "80ffffff"))
                .foregroundColor(Color.black)
                .font(.system(size: 30, weight: .bold))
                
                Button("Rook") {
                    gameManager.selectPromotionPiece(piece: "rook", boardNumber: boardNumber)
                }
                .frame(width: squareSize*8, height: squareSize * 1.5)
                .background(Color.init(hex: "80ffffff"))
                .foregroundColor(Color.black)
                .font(.system(size: 30, weight: .bold))
                
                Button("Bishop") {
                    gameManager.selectPromotionPiece(piece: "bishop", boardNumber: boardNumber)
                }
                .frame(width: squareSize*8, height: squareSize * 1.5)
                .background(Color.init(hex: "80ffffff"))
                .foregroundColor(Color.black)
                .font(.system(size: 30, weight: .bold))
                
                Button("Knight") {
                    gameManager.selectPromotionPiece(piece: "knight", boardNumber: boardNumber)
                }
                .frame(width: squareSize*8, height: squareSize * 1.5)
                .background(Color.init(hex: "80ffffff"))
                .foregroundColor(Color.black)
                .font(.system(size: 30, weight: .bold))
                
            }.rotationEffect(.degrees(getRotation()))
        }
        

    }
    
    
    private func getRotation() -> Double
    {
        
        if boardNumber == 0
        {
            if gameManager.pawnPromotionColor[boardNumber] == "white" { return 90.0 }
            if gameManager.pawnPromotionColor[boardNumber] == "black" { return 270.0 }
        }
        else
        {
            if gameManager.pawnPromotionColor[boardNumber] == "white" { return 270.0 }
            if gameManager.pawnPromotionColor[boardNumber] == "black" { return 90.0 }
        }
        return 0.0
    }
    
}


