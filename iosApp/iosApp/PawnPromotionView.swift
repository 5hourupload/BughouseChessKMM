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
    var color: String
    var gameManager: GameManager
    @State var show: Bool

    
    init(boardNumber: Int, color: String, gameManager: GameManager, show: Bool) {
        
        self.boardNumber = boardNumber
        self.color = color
        self.gameManager = gameManager
        self.show = show


    }
    
    var body: some View
    {
        let squareSize = UIScreen.main.bounds.width / 10

        if gameManager.showPawnOptions[boardNumber] {
            VStack(spacing: 10)
            {
                Button("Queen") {
                    gameManager.selectPromotionPiece(piece: "queen", boardNumber: boardNumber)
                }.frame(width: squareSize*8, height: squareSize * 1.5).background(Color.init(hex: "80ffffff"))
                
                Button("Rook") {
                    gameManager.selectPromotionPiece(piece: "rook", boardNumber: boardNumber)
                }.frame(width: squareSize*8, height: squareSize * 1.5).background(Color.init(hex: "80ffffff"))
                
                Button("Bishop") {
                    gameManager.selectPromotionPiece(piece: "bishop", boardNumber: boardNumber)
                }.frame(width: squareSize*8, height: squareSize * 1.5).background(Color.init(hex: "80ffffff"))
                
                Button("Knight") {
                    gameManager.selectPromotionPiece(piece: "knight", boardNumber: boardNumber)
                }.frame(width: squareSize*8, height: squareSize * 1.5).background(Color.init(hex: "80ffffff"))
            }
        }
        

    }
    
}

