package com.cjproductions.vicinity.discover.data.local.model

import com.cjproductions.vicinity.discover.domain.model.Segment
import comcjproductionsvicinitycoredatadatabase.SegmentEntity

fun SegmentEntity.toSegment(): Segment {
  return Segment(
    id = id,
    name = name,
  )
}