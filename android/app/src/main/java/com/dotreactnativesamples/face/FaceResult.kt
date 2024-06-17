package com.dotreactnativesamples.face

import com.innovatrics.dot.face.detection.DetectedFace
import com.innovatrics.dot.face.quality.EyesExpression
import com.innovatrics.dot.face.quality.FaceAspects
import com.innovatrics.dot.face.quality.FaceAttribute
import com.innovatrics.dot.face.quality.FaceImageQuality
import com.innovatrics.dot.face.quality.HeadPose
import com.innovatrics.dot.face.quality.Wearables

data class FaceResult(
    val confidence: Double?,
    val faceAspects: FaceAspects?,
    val faceQuality: FaceQuality?
)

data class FaceQuality(
    val headPose: HeadPose?,
    val imageQuality: FaceImageQuality?,
    val wearables: Wearables?,
    val expression: Expression?
)

data class Expression(
    val eyes: EyesExpression?,
    val mouth: FaceAttribute?
)

fun DetectedFace.resolve(): FaceResult {
    return FaceResult(
        confidence = getConfidence(),
        faceAspects = evaluateFaceAspects(),
        faceQuality = convertFaceQuality(evaluateFaceQuality())
    )
}

private fun convertFaceQuality(faceQuality: com.innovatrics.dot.face.quality.FaceQuality): FaceQuality {
    return FaceQuality(
        headPose = faceQuality.headPose,
        imageQuality = faceQuality.imageQuality,
        wearables = faceQuality.wearables,
        expression = faceQuality.expression?.let { convertExpression(it) }
    )
}

private fun convertExpression(expression: com.innovatrics.dot.face.quality.Expression): Expression {
    return Expression(
        eyes = expression.eyes,
        mouth = expression.mouth,
    )
}
