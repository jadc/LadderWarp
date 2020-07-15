package red.jad.ladderwarp;

import net.minecraft.block.*;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LadderBlock.class, VineBlock.class, TwistingVinesPlantBlock.class, WeepingVinesPlantBlock.class, TwistingVinesBlock.class, WeepingVinesBlock.class})
public class WarpClimbableMixin {

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if(!player.isSneaking()){
            int up = 0, down = 0;
            while(world.getBlockState(pos.add(0, up + 1, 0)).getBlock().isIn(BlockTags.CLIMBABLE)) up++;
            while(world.getBlockState(pos.add(0, down - 1, 0)).getBlock().isIn(BlockTags.CLIMBABLE)) down--;
            if(world.getBlockState(pos.add(0, up + 1, 0)).getCollisionShape(world, pos.add(0, up + 1, 0)) != null) up--;
            if(up > 0 || down < 0){
                int change = up >= Math.abs(down) ? up : down;
                player.teleport(pos.getX() + 0.5, pos.getY() + change, pos.getZ() + 0.5);
                world.playSound(null, player.getBlockPos(), world.getBlockState(pos).getBlock().getSoundGroup(world.getBlockState(pos)).getStepSound(), SoundCategory.BLOCKS, 0.25f, 1.0f);
            }
        }

        return ActionResult.SUCCESS;
    }
}
