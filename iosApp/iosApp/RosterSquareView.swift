//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct RosterSquareView: View {
    @State var rotation: Double = 90
    var i: Int
    var boardNumber: Int
    var color: String
    var gameManager: GameManager
    var backgroundImage: UIImage
    @State var square: RosterSquare
    
    init(i: Int, boardNumber: Int, color: String, gameManager: GameManager, square: RosterSquare) {
        let squareSize = UIScreen.main.bounds.width / 10

        if (i.isMultiple(of: 2)) {
            backgroundImage = UIColor(hex: "#B0B0B080")!.image(CGSize(width: squareSize, height: squareSize))
        }
        else {
            backgroundImage = UIColor(hex: "#8E8E8E80")!.image(CGSize(width: squareSize, height: squareSize))
        }
        
        self.i = i
        self.boardNumber = boardNumber
        self.color = color
        self.gameManager = gameManager
        self.square = square

    }
    
    var body: some View
    {
        ZStack{
            Image(uiImage: backgroundImage)
            
            Image(uiImage: square.getCosmetic())
            
            Image(uiImage: square.getUIImage())
        }

    }
    
}

