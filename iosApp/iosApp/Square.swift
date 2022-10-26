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
    
    public func getCosmetic() -> UIImage
    {
        let squareSize = UIScreen.main.bounds.width / 10
        if (cosmetic == "dot")
        {
            return (UIImage(named: "dot")?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!

        }
        else if (cosmetic == "yellow")
        {
            return UIColor.yellow.image(CGSize(width: squareSize, height: squareSize))
        }
        else if (cosmetic == "red")
        {
            return UIColor.red.image(CGSize(width: squareSize, height: squareSize))
        }
        else
        {
            return (UIImage(named: "nothing"))!
        }
    }
}
extension UIColor {
    func image(_ size: CGSize = CGSize(width: 1, height: 1)) -> UIImage {
        return UIGraphicsImageRenderer(size: size).image { rendererContext in
            self.setFill()
            rendererContext.fill(CGRect(origin: .zero, size: size))
        }
    }
}
