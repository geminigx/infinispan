package org.infinispan.server.hotrod

import org.infinispan.server.core.Logging
import org.infinispan.server.core.transport.{ChannelBuffer, ChannelHandlerContext, Channel, Encoder}
import OperationStatus._

/**
 * // TODO: Document this
 * @author Galder Zamarreño
 * @since
 */

class HotRodEncoder extends Encoder {
   import HotRodEncoder._

   override def encode(ctx: ChannelHandlerContext, channel: Channel, msg: AnyRef): AnyRef = {
      trace("Encode msg {0}", msg)
      val buffer: ChannelBuffer = msg match {
         case r: Response => writeHeader(ctx.getChannelBuffers.dynamicBuffer, r)
//         case s: StatsResponse => {
//            val buffer = ctx.getChannelBuffers.dynamicBuffer
//            for ((key, value) <- s.stats) {
//               writeHeader(buffer, s)
//               buffer.writeString(key)
//               buffer.writeString(value)
//            }
//         }
      }
      msg match {
         case g: GetWithVersionResponse => {
            if (g.status == Success) {
               buffer.writeLong(g.version)
               buffer.writeRangedBytes(g.data.get)
            }
         }
         case g: GetResponse => if (g.status == Success) buffer.writeRangedBytes(g.data.get)
         case e: ErrorResponse => buffer.writeString(e.msg)
         case _ => if (buffer == null) throw new IllegalArgumentException("Response received is unknown: " + msg);         
      }
      buffer
   }

   private def writeHeader(buffer: ChannelBuffer, r: Response): ChannelBuffer = {
      buffer.writeByte(Magic.byteValue)
      buffer.writeUnsignedLong(r.messageId)
      buffer.writeByte(r.operation.id.byteValue)
      buffer.writeByte(r.status.id.byteValue)
      buffer.writeByte(0) // topology change marker
      buffer
   }
   
}

object HotRodEncoder extends Logging {
   private val Magic = 0xA1
}