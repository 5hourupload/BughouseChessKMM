//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct BoardSquareView: View {
    @State var rotation: Double = 90
    var x: Int
    var y: Int
    var boardNumber: Int
    var gameManager: GameManager
    var backgroundImage: UIImage
    
    init(x: Int, y: Int, boardNumber: Int, gameManager: GameManager) {
        let squareSize = UIScreen.main.bounds.width / 10
        if ((x + y).isMultiple(of: 2)) {
            backgroundImage = (UIImage(named: "black")?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!
        }
        else {
            backgroundImage = (UIImage(named: "white")?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!
        }
        
        self.x = x
        self.y = y
        self.boardNumber = boardNumber
        self.gameManager = gameManager
    }
    
    var body: some View
    {
        ZStack{
            Image(uiImage: backgroundImage)
            
            Image(uiImage: gameManager.board[boardNumber][x][y].getCosmetic())
            
            Image(uiImage: gameManager.board[boardNumber][x][y].getUIImage())
        }

    }
    
}

