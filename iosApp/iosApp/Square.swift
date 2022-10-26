//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/26/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import Combine

struct Square {
    
    
    var cosmetic: String = "none"
    var piece: Piece = Empty()
    
    
//    init() {
//    }
    
    public func getUIImage() -> UIImage
    {
        let squareSize = UIScreen.main.bounds.width / 10
        if (piece.color == "white")
        {
            return (UIImage(named: piece.type!)?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!

        }
        else if (piece.color == "black")
        {
            return (UIImage(named: "b" + piece.type!)?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!

        }
        else
        {
            return (UIImage(named: "nothing"))!
        }
    }
}
